use core::f64;
use gnuplot::{Color, Figure};
use rand::Rng;
use std::{error::Error, io, ops, process, usize};

fn main() {
    let mut _data = Vec::new();
    if let Err(err) = read(&mut _data) {
        println!("error running example: {}", err);
        process::exit(1);
    }
    let data = _data;
    let m = 3;
    let (mut cluser_set, mut cluster) = k_means(&data, m);
    let mut var = calc_varience(&data, &cluster, m);
    for _ in 0..1000 {
        let (ncluser_set, ncluster) = k_means(&data, m);
        let mut kinds = vec![0; m];
        for i in &ncluster {
            kinds[*i] = 1;
        }
        if kinds.iter().sum::<usize>() != m {
            continue;
        }
        let nvar = calc_varience(&data, &ncluster, m);
        if nvar < var {
            var = nvar;
            cluster = ncluster;
            cluser_set = ncluser_set;
        }
    }
    println!("cluster :{:?}", cluster);
    let mut fig = Figure::new();
    let colors = ["red", "blue", "orange", "green", "black"];
    {
        let ax = fig.axes2d();
        for i in 0..m {
            if cluser_set[i].is_empty() {
                continue;
            }
            let mut dat_x = Vec::new();
            let mut dat_y = Vec::new();
            for p in &cluser_set[i] {
                dat_x.push(p.x);
                dat_y.push(p.y);
            }
            println!("\ncluster {}", i);
            println!("data x: {:?}", dat_x);
            println!("data y: {:?}", dat_y);
            ax.points(&dat_x, &dat_y, &[Color(colors[i])]);
        }
    }
    fig.set_terminal("png", "plot.png");
    let _ = fig.show();
}

fn calc_varience(data: &Vec<Point>, cluster: &Vec<usize>, m: usize) -> f64 {
    let center = calc_center(data, cluster, m);
    let mut varience = vec![0.; m];
    let mut count = vec![0.; m];
    for i in 0..cluster.len() {
        let x_bar = center[cluster[i]].x;
        let y_bar = center[cluster[i]].y;
        varience[cluster[i]] += (data[i].x - x_bar) * (data[i].y - y_bar);
        count[cluster[i]] += 1.;
    }
    for i in 0..m {
        if count[i] != 0. {
            varience[i] /= count[i];
        }
    }
    varience.iter().sum::<f64>() / (m as f64)
}

fn k_means(data: &Vec<Point>, m: usize) -> (Vec<Vec<Point>>, Vec<usize>) {
    let n = data.len();
    let mut rng = rand::thread_rng();
    let mut cluster = (0..n)
        .into_iter()
        .map(|_| rng.gen_range(0..m))
        .collect::<Vec<_>>();
    let mut center = calc_center(&data, &cluster, m);
    loop {
        let mut new_cluser = vec![0; n];
        for i in 0..n {
            let mut d_min = 1e10;
            for j in 0..m {
                let d_cur = calc_dist(data[i], center[j]);
                new_cluser[i] = if d_min > d_cur {
                    d_min = d_cur;
                    j
                } else {
                    new_cluser[i]
                };
            }
        }
        if cluster == new_cluser {
            break;
        }
        cluster = new_cluser;
        center = calc_center(&data, &cluster, m);
    }
    let mut cluster_set: Vec<Vec<Point>> = vec![Vec::new(); m];
    for i in 0..n {
        cluster_set[cluster[i]].push(data[i]);
    }
    (cluster_set, cluster)
}
fn calc_dist(a: Point, b: Point) -> f64 {
    let dx = a.x - b.x;
    let dy = a.y - b.y;
    (dx * dx + dy * dy).sqrt()
}
fn calc_center(data: &Vec<Point>, cluster: &Vec<usize>, m: usize) -> Vec<Point> {
    let mut center = vec![Point { x: 0., y: 0. }; m];
    let mut count = vec![0.; m];
    for i in 0..center.len() {
        center[cluster[i]] += data[i];
        count[cluster[i]] += 1.;
    }
    for i in 0..m {
        center[i].x /= count[i];
        center[i].y /= count[i];
    }
    center
}
#[derive(Debug, Copy, Clone)]
struct Point {
    x: f64,
    y: f64,
}
impl ops::Add for Point {
    type Output = Self;
    fn add(self, other: Self) -> Self {
        Self {
            x: self.x + other.x,
            y: self.y + other.y,
        }
    }
}
impl ops::AddAssign for Point {
    fn add_assign(&mut self, other: Self) {
        *self = Self {
            x: self.x + other.x,
            y: self.y + other.y,
        };
    }
}

fn read(data: &mut Vec<Point>) -> Result<(), Box<dyn Error>> {
    let mut rdr = csv::Reader::from_reader(io::stdin());
    for result in rdr.records() {
        let record = result?;
        let x = record[1].parse::<f64>().unwrap();
        let y = record[2].parse::<f64>().unwrap();
        data.push(Point { x, y });
    }
    Ok(())
}
