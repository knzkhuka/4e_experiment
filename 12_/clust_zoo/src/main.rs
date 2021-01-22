use rand::Rng;
use std::error::Error;
use std::io;
use std::process;

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
    #[warn(dead_code)]
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
                roots.push(i);
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

struct data_t {
    name: String,
    value: Vec<f64>,
}

fn dist(a: &data_t, b: &data_t) -> f64 {
    let weight = [
      // miss 3 : 1.,1.,1.,1.,1.,1.,1.,1.,1.,1.,1.,1.,1.,1.,1.,1.,
      // miss 2 : 1.,5.,2.,2.,2.,2.,2.,1.,2.,2.,3.,4.,1.,2.,3.,1.,
      2.,2.,1.,3.,5.,2.,3.,3.,5.,2.,4.,5.,4.,1.,5.,1.,
    ];
    let n: usize = a.value.len();
    let mut d: f64 = 0.0;
    for i in 0..n {
        let dv: f64 = a.value[i] - b.value[i];
        d += dv * dv * weight[i];
    }
    d.sqrt()
}

fn read(data: &mut Vec<data_t>, ans: &mut Vec<usize>) -> Result<(), Box<dyn Error>> {
    let mut rdr = csv::Reader::from_reader(io::stdin());
    for result in rdr.records() {
        let record = result?;
        let n: usize = record.len();
        let name: String = record[0].to_string();
        let mut value: Vec<f64> = (1..n - 1)
            .collect::<Vec<usize>>()
            .iter()
            .map(|i| record[*i].parse::<f64>().unwrap())
            .collect();
        value[12] /= 10.;
        let d = data_t { name, value };
        data.push(d);
        ans.push(record[n - 1].parse::<usize>().unwrap());
    }
    Ok(())
}
fn main() {
    let mut data: Vec<data_t> = Vec::new();
    let mut ans: Vec<usize> = Vec::new();
    if let Err(err) = read(&mut data, &mut ans) {
        println!("error running example: {}", err);
        process::exit(1);
    }

    let n: usize = data.len();
    let mut edges: Vec<(f64, usize, usize)> = Vec::new();
    for i in 0..n {
        for j in i..n {
            edges.push((dist(&data[i], &data[j]), i, j));
        }
    }
    edges.sort_by(|a, b| a.partial_cmp(b).unwrap());
    let m = 7;
    let mut uni = Unionfind::new(n);
    for (_, u, v) in edges {
        if uni.tree_count == m {
            break;
        }
        uni.unite(u, v);
    }
    let r_st = uni.root_set();
    let mut clusters: Vec<Vec<usize>> = Vec::new();
    for r in r_st {
        clusters.push(uni.group(r));
    }

    for i in 0..clusters.len() {
        println!("cluster {}:", i);
        for j in &clusters[i] {
            print!("{}, ", data[*j].name);
        }
        println!();
    }

    let mut tmp: Vec<usize> = Vec::new();
    for i in 0..n {
        tmp.push(uni.root(i));
    }
    tmp.sort();
    tmp.dedup();
    for i in 0..n {
        print!("{},", tmp.binary_search(&uni.root(i)).unwrap());
    }
    println!();

    let mut mapping: Vec<usize> = vec![10; 10];
    for i in 0..n {
        mapping[ans[i]] = tmp.binary_search(&uni.root(i)).unwrap();
    }
    let mut miss = 0;
    for i in 0..n {
        if mapping[ans[i]] != tmp.binary_search(&uni.root(i)).unwrap() {
            miss += 1;
        }
    }
    println!("{}", miss);

    let mut rng = rand::thread_rng();
    for i in 0..16 {
        print!("{}.,", rng.gen::<u32>() % 5 + 1);
    }
    println!();
}
