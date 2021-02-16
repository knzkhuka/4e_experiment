use gnuplot::{Color, Figure, Graph, PlotOption::Caption};
use std::{error::Error, io, ops, process};

fn main() {
    let mut data = Vec::new();
    if let Err(err) = read(&mut data) {
        println!("error running example: {}", err);
        process::exit(1);
    }
    let m = 4;
    let cluster = min_dist_method(&data, m);
    let cluster_set = construst_clusterset(&data, &cluster, m);
    savefig(&cluster_set, "res".to_string());
}

fn savefig(cluster_set: &Vec<Vec<Point>>, filename: String) {
    let m = cluster_set.len();
    let mut fig = Figure::new();
    let _colors = ["red", "blue", "orange", "green", "black"];
    {
        let ax = fig.axes2d();
        for i in 0..m {
            if cluster_set[i].is_empty() {
                continue;
            }
            let mut dat_x = Vec::new();
            let mut dat_y = Vec::new();
            for p in &cluster_set[i] {
                dat_x.push(p.x);
                dat_y.push(p.y);
            }
            ax.points(
                &dat_x,
                &dat_y,
                &[
                    Caption(&i.to_string()), //Color(colors[i % colors.len()])
                ],
            );
            ax.set_legend(Graph(1.), Graph(0.3), &[], &[]);
        }
        let mut dat_x = Vec::new();
        let mut dat_y = Vec::new();
        for p in calc_center(cluster_set) {
            dat_x.push(p.x);
            dat_y.push(p.y);
        }
        ax.points(&dat_x, &dat_y, &[Caption("center")]);
    }
    fig.set_terminal("png", &format!("./{}.png", filename).to_string()[..]);
    let _ = fig.show();
}

fn calc_center(cluster_set: &Vec<Vec<Point>>) -> Vec<Point> {
    let mut center = Vec::new();
    let m = cluster_set.len();
    for i in 0..m {
        let mut c = cluster_set[i].iter().fold(Point::new(), |acc, p| acc + *p);
        let n: f64 = cluster_set[i].len() as f64;
        c.x /= n;
        c.y /= n;
        center.push(c);
    }
    center
}

fn construst_clusterset(data: &Vec<Point>, cluster: &Vec<usize>, m: usize) -> Vec<Vec<Point>> {
    let n = cluster.len();
    let mut cluster_set = vec![Vec::new(); m];
    for i in 0..n {
        cluster_set[cluster[i]].push(data[i])
    }
    cluster_set
}

fn min_dist_method(data: &Vec<Point>, m: usize) -> Vec<usize> {
    let n = data.len();

    let mut edges: Vec<(f64, usize, usize)> = Vec::new();
    for i in 0..n {
        for j in i + 1..n {
            edges.push((calc_dist(data[i], data[j]), i, j));
        }
    }
    edges.sort_by(|a, b| a.partial_cmp(b).unwrap());

    let mut uni = Unionfind::new(n);
    for (_, i, j) in edges {
        if uni.tree_count == m {
            break;
        }
        uni.unite(i, j);
    }

    let mut cluster = (0..n)
        .into_iter()
        .map(|i| uni.root(i))
        .collect::<Vec<usize>>();
    cluster = compression(cluster);
    cluster
}

fn compression(data: Vec<usize>) -> Vec<usize> {
    let n = data.len();
    let mut v = data.clone();
    v.sort();
    v.dedup();
    (0..n)
        .into_iter()
        .map(|i| v.binary_search(&data[i]).unwrap())
        .collect::<Vec<usize>>()
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

#[derive(Debug, Copy, Clone)]
struct Point {
    x: f64,
    y: f64,
}
impl Point {
    fn new() -> Point {
        Point { x: 0., y: 0. }
    }
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
fn calc_dist(a: Point, b: Point) -> f64 {
    let dx = a.x - b.x;
    let dy = a.y - b.y;
    (dx * dx + dy * dy).sqrt()
}

struct Unionfind {
    par: Vec<usize>,
    size: Vec<usize>,
    tree_count: usize,
}
impl Unionfind {
    fn new(n: usize) -> Unionfind {
        let mut vec = vec![0; n];
        for i in 0..n {
            vec[i] = i;
        }
        Unionfind {
            par: vec,
            size: vec![1; n],
            tree_count: n,
        }
    }
    fn root(&mut self, k: usize) -> usize {
        if k == self.par[k] {
            k
        } else {
            let k_par = self.par[k];
            let k_root = self.root(k_par);
            self.par[k] = k_root;
            k_root
        }
    }
    fn unite(&mut self, u: usize, v: usize) -> bool {
        let mut u_par = self.root(u);
        let mut v_par = self.root(v);
        if u_par == v_par {
            return false;
        }
        if self.size[u_par] < self.size[v_par] {
            std::mem::swap(&mut u_par, &mut v_par);
        }
        self.par[v_par] = u_par;
        self.size[u_par] += self.size[v_par];
        self.tree_count -= 1;
        true
    }
}
