from math import pi
from sympy import *

def gauss(a,b,f,n):
  init_printing()
  x = Symbol('x')
  P = legendre(n,x)
  dP = diff(P)
  res = solve(P,x)
  X = [N(r) for r in res]
  W = [2/((1-xi**2)*(dP.subs(x,xi)**2)) for xi in X]
  print(X,W)
  tmp = (b-a)/2*sum(W[i]*f((b-a)/2*X[i]+(b+a)/2) for i in range(n))
  return tmp

def f(x):return (1-x**2)**0.5

ns = list(range(40))
gau = [4*gauss(0,1,f,n) for n in ns]