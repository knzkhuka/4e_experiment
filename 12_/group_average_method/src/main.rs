use std::io;
use std::{collections::HashMap, process};
use std::{error::Error, usize};

use gnuplot::{Color, Figure};

#[derive(Debug, Copy, Clone)]
struct Point {
    x: f64,
    y: f64,
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

fn main() {
    let mut data = Vec::new();
    if let Err(err) = read(&mut data) {
        println!("error running example: {}", err);
        process::exit(1);
    }
    let m = 9;
    let cluster = group_average_method(data, m);
    for (i, clst) in &cluster {
        println!("{},{}", i, clst.len());
    }
    let colors = ["red", "blue", "green", "black", "orange", "yellow"];
    let mut fig = Figure::new();
    {
        let ax = fig.axes2d();
        let mut i = 0;
        for (_, dat) in cluster {
            let mut dat_x = Vec::new();
            let mut dat_y = Vec::new();
            for point in dat {
                dat_x.push(point.x);
                dat_y.push(point.y);
            }
            ax.points(&dat_x, &dat_y, &[Color(colors[i])]);
            i += 1;
            i %= colors.len();
        }
    }
    fig.set_terminal("png", "plot.png");
    let _ = fig.show();
}
fn group_average_method(data: Vec<Point>, m: usize) -> HashMap<usize, Vec<Point>> {
    let mut cluster: HashMap<usize, Vec<Point>> = HashMap::new();
    let n = data.len();
    for i in 0..n {
        let vi = vec![data[i]];
        cluster.insert(i, vi);
    }
    let mut indexes = (0..n).into_iter().collect::<Vec<_>>();
    while cluster.len() > m {
        let (mut mi, mut mj) = (0, 0);
        let mut mdist = 1e10;
        for i in &indexes {
            for j in &indexes {
                if i < j {
                    let dist = calc_dist_average(&cluster[&i], &cluster[&j]);
                    if mdist > dist {
                        mdist = dist;
                        mi = *i;
                        mj = *j;
                    }
                }
            }
        }
        let mut clst_j = cluster.remove(&mj).unwrap();
        cluster.get_mut(&mi).unwrap().append(&mut clst_j);
        let rmidx = indexes.iter().position(|x| *x == mj).unwrap();
        indexes.remove(rmidx);
    }
    cluster
}
fn calc_dist_average(a: &Vec<Point>, b: &Vec<Point>) -> f64 {
    let mut sum_d: f64 = 0.;
    for ai in a {
        for bj in b {
            sum_d += calc_dist_point(*ai, *bj);
        }
    }
    sum_d / ((a.len() * b.len()) as f64)
}
fn calc_dist_point(a: Point, b: Point) -> f64 {
    let dx = a.x - b.x;
    let dy = a.y - b.y;
    (dx * dx + dy * dy).sqrt()
}
