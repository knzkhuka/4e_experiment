use gnuplot::{Caption, Color, Figure, PointSize, PointSymbol};
use std::error::Error;
use std::io;
use std::process;

#[derive(Debug)]
struct Point {
  x: f64,
  y: f64,
}

fn read(data: &mut Vec<Point>) -> Result<(), Box<dyn Error>> {
  let mut rdr = csv::Reader::from_reader(io::stdin());
  for result in rdr.records() {
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
/*
fn calc_dist(a: Point, b: Point) -> f64 {
  let dx = a.x - b.x;
  let dy = a.y - b.y;
  (dx * dx + dy * dy as f64).sqrt()
}
*/

fn main() {
  let mut data: Vec<Point> = Vec::with_capacity(100);
  if let Err(err) = read(&mut data) {
    println!("error running example: {}", err);
    process::exit(1);
  }
  let mut dat_x: Vec<f64> = Vec::new();
  let mut dat_y: Vec<f64> = Vec::new();
  for p in data {
    dat_x.push(p.x);
    dat_y.push(p.y);
  }
  let limit_x = [0, 10];
  let limit_y = [0, 10];
  let mut fg = Figure::new();
  {
    let x = fg.axes2d();
    x.points(&dat_x, &dat_y, &[Caption("sample points"), Color("red")]);
    x.points(&limit_x, &limit_y, &[PointSymbol('+'), Color("black")]);
  }
  fg.set_terminal("png", "plot.png");
  let _ = fg.show();
}
