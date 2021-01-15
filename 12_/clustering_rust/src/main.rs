// /https://github.com/knzkhuka/4e_experiment/tree/main/12_/clustering_rust

use gnuplot::{Caption, Color, Figure};
use std::error::Error;
use std::io;
use std::process;

#[derive(Debug)]
struct Point {
  x: f64,
  y: f64,
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
  fn same(&mut self, u: usize, v: usize) -> bool {
    self.root(u) == self.root(v)
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
  fn root_set(&mut self) -> Vec<usize> {
    let mut roots: Vec<usize> = Vec::new();
    for i in 0..self.par.len() {
      if i == self.par[i] {
        roots.push(i)
      }
    }
    roots
  }
  fn group(&mut self, r: usize) -> Vec<usize> {
    let mut g: Vec<usize> = Vec::new();
    for i in 0..self.par.len() {
      if self.root(i) == r {
        g.push(i);
      }
    }
    g
  }
}

fn read(data: &mut Vec<Point>) -> Result<(), Box<dyn Error>> {
  let mut rdr = csv::Reader::from_reader(io::stdin());
  let mut head = 0;
  for result in rdr.records() {
    if head == 0 {
      head += 1;
      continue;
    }
    let record = result?;
    let x_str = &record[1];
    let y_str = &record[2];
    let x: f64 = x_str.parse::<f64>().unwrap();
    let y: f64 = y_str.parse::<f64>().unwrap();
    let p = Point { x, y };
    data.push(p);
  }
  Ok(())
}

fn calc_dist(a: &Point, b: &Point) -> f64 {
  let dx = a.x - b.x;
  let dy = a.y - b.y;
  (dx * dx + dy * dy as f64).sqrt()
}

fn main() {
  let mut data: Vec<Point> = Vec::new();
  if let Err(err) = read(&mut data) {
    println!("error running example: {}", err);
    process::exit(1);
  }
  let n: usize = data.len();
  let mut edges: Vec<(f64, usize, usize)> = Vec::new();
  for i in 0..n {
    for j in i..n {
      edges.push((calc_dist(&data[i], &data[j]), i, j));
    }
  }
  edges.sort_by(|a, b| a.partial_cmp(b).unwrap());

  // クラスタ数を変えるときはここを変える
  let m = 3;

  let mut uni = Unionfind::new(n);
  for (cost, u, v) in edges {
    if uni.tree_count == m {
      break;
    }
    uni.unite(u, v);
  }
  let R = uni.root_set();

  let mut clusters: Vec<Vec<usize>> = Vec::new();
  for r in R {
    clusters.push(uni.group(r));
  }
  assert_eq!(clusters.len(), m);

  let colors = ["red", "blue", "green", "black"];
  let mut fg = Figure::new();
  {
    let x = fg.axes2d();
    for i in 0..m {
      let mut dat_x: Vec<f64> = Vec::new();
      let mut dat_y: Vec<f64> = Vec::new();
      for j in &clusters[i] {
        dat_x.push(data[*j].x);
        dat_y.push(data[*j].y);
      }
      x.points(&dat_x, &dat_y, &[Color(colors[i])]);
    }
  }
  fg.set_terminal("png", "plot.png");
  let _ = fg.show();
}
