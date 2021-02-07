use gnuplot::{Color, Figure};
use rand::Rng;
use std::io;
use std::{collections::HashMap, process};
use std::{error::Error, usize};

fn main() {}

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
