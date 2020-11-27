def central(X,f,h):
  return [(f(x+h)-f(x-h))/(2*h) for x in X]
def backward(X,f,h):
  return [(f(x)-f(x-h))/h for x in X]
def forward(X,f,h):
  return [(f(x+h)-f(x))/h for x in X]

import numpy as np
import matplotlib.pyplot as plt
from math import pi

x = np.arange(0,2*pi,0.1)
y_sin = np.sin(x)
y_cos = np.cos(x)

def f(x):return np.sin(x)
h = 0.1

diff_central = central(x,f,h)
diff_backward = backward(x,f,h)
diff_forward = forward(x,f,h)

plt.plot(x,y_sin,label='sin')
plt.plot(x,y_cos,label='cos')
plt.plot(x,diff_central,label='central',linestyle='dotted')
plt.plot(x,diff_backward,label='backward',linestyle='dotted')
plt.plot(x,diff_forward,label='foward',linestyle='dotted')

plt.grid()
plt.legend()
plt.show()