def central(X,f,h):
  return [(f(x+h)-f(x-h))/(2*h) for x in X]
def backward(X,f,h):
  return [(f(x)-f(x-h))/h for x in X]
def forward(X,f,h):
  return [(f(x+h)-f(x))/h for x in X]

import numpy as np
import matplotlib.pyplot as plt
from math import pi

x = np.arange(0,pi*1.5,0.001)
y_sin = np.sin(x)
y_cos = np.cos(x)

def f(x):return np.sin(x)
h = 0.1

diff_central = central(x,f,h)
diff_backward = backward(x,f,h)
diff_forward = forward(x,f,h)

plt.plot(x,y_sin,label='sin')
#plt.plot(x,y_cos,label='cos')
#plt.plot(x,diff_central,label='central',linestyle='dotted')
#plt.plot(x,diff_backward,label='backward',linestyle='dotted')
#plt.plot(x,diff_forward,label='forward',linestyle='dotted')

idx = 1200
coef_cos = y_cos[idx]
coef_cen = diff_central[idx]
coef_for = diff_forward[idx]
coef_bac = diff_backward[idx]
xx = np.arange(-2,2,0.001)
xxx = [x[idx]+xi for xi in xx]
cos_yy = [y_sin[idx]+xi*coef_cos for xi in xx]
cen_yy = [y_sin[idx]+xi*coef_cen for xi in xx]
for_yy = [y_sin[idx]+xi*coef_for for xi in xx]
bac_yy = [y_sin[idx]+xi*coef_bac for xi in xx]
yy = [y_sin[idx]+xi for xi in xx]
plt.plot(xxx,cos_yy,label="coef of cos")
plt.plot(xxx,yy,label="y=x+b")
plt.plot(xxx,cen_yy,label="coef of ccentral",linestyle='dotted')
plt.plot(xxx,for_yy,label="coef of forward",linestyle='dotted')
plt.plot(xxx,bac_yy,label="coef of backward",linestyle='dotted')
"""
def err_rate(m,t):return abs(m-t)/t
er_rt = [100*err_rate(diff_central[i],y_cos[i])for i in range(len(y_cos))]

fig = plt.figure()
ax1 = fig.add_subplot(111)
ax1.plot(x,y_cos,label='cos(x)')
ax1.plot(x,diff_central,label='central',linestyle='dotted')
ax2 = ax1.twinx()
ax2.plot(x,er_rt,'r',label='error rate')

h1, l1 = ax1.get_legend_handles_labels()
h2, l2 = ax2.get_legend_handles_labels()
ax1.legend(h1+h2, l1+l2)

ax1.set_ylabel('f(x)')
ax2.set_ylabel('error rate[%]')
plt.ylim(-40,40)
ax1.grid()
"""
plt.legend()
plt.grid()
plt.show()