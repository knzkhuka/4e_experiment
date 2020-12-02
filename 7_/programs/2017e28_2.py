import numpy as np
import matplotlib.pyplot as plt
from math import pi

def integral_based_trapezoidal_rule(a,b,f,n):
  h = (b-a)/n
  return sum((f(a+h*(i+1))+f(a+h*i))*h/2 for i in range(n))

def simpsons_rule(a,b,f):
  return (b-a)/6*(f(a)+4*f((a+b)/2)+f(b))

def integral_based_simpsons_rule(a,b,f,n):
  h = (b-a)/n
  return sum(simpsons_rule(a+h*i,a+h*(i+1),f) for i in range(n))

def f(x):return (1-x**2)**0.5
N = [1,10,100,1000,10000]
tra = [4*integral_based_trapezoidal_rule(0,1,f,n) for n in N]
sim = [4*integral_based_simpsons_rule(0,1,f,n) for n in N]

print("pi = {}".format(pi))
print("trapezoidal_rule")
for i in range(len(N)):
  print("N = {0:6} : pi = {1:.6f} : err = {2:10.6f} [%]".format(N[i],tra[i],100*(tra[i]-pi)/pi))
print("simpsons_rule")
for i in range(len(N)):
  print("N = {0:6} : pi = {1:.6f} : err = {2:10.6f} [%]".format(N[i],sim[i],100*(sim[i]-pi)/pi))

def g(x):return x**15
N = [1,10,100,1000,10000]
tra = [integral_based_trapezoidal_rule(-12,34,g,n) for n in N]
sim = [integral_based_simpsons_rule(-12,34,g,n) for n in N]
print(*map(int,tra),sep='\n')
print(*map(int,sim),sep='\n')