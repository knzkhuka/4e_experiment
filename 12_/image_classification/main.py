import cv2
from union_find import UnionFind
from math import sqrt
import numpy
numpy.set_printoptions(threshold=numpy.inf)


def classification(b, g, r):
  base = 256 / 4
  result = 0
  result += int(b // base)
  result += int(g // base * 4)
  result += int(r // base * 16)
  return result


def read(image):
  img = cv2.imread(image)
  count = [0] * 64
  for hoge in img:
    for fuga in hoge:
      count[classification(*fuga)] += 1
  # print(count)
  return count
  """
  gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
  ret, img_thres = cv2.threshold(gray, 0, 255, cv2.THRESH_OTSU)
  black, white = 0, 0
  for hoge in img_thres:
    for fuga in hoge:
      if fuga == 0:
        black += 1
      else:
        white += 1
  return (black, white)
  """


images = ["./foods/1088332.jpg", "./foods/1206166.jpg", "./foods/125490.jpg", "./foods/1259816.jpg", "./foods/129666.jpg", "./foods/1380247.jpg", "./foods/141056.jpg", "./foods/148341.jpg", "./foods/1615453.jpg", "./foods/187303.jpg", "./foods/188830.jpg", "./foods/2031970.jpg", "./foods/211459.jpg", "./foods/215480.jpg", "./foods/242813.jpg", "./foods/2466030.jpg", "./foods/249983.jpg", "./foods/2742044.jpg", "./foods/277935.jpg", "./foods/284097.jpg", "./foods/289490.jpg", "./foods/2948069.jpg", "./foods/299780.jpg", "./foods/3138.jpg", "./foods/331860.jpg", "./foods/336447.jpg", "./foods/351391.jpg", "./foods/381307.jpg", "./foods/415551.jpg", "./foods/479711.jpg", "./foods/489347.jpg", "./foods/513129.jpg", "./foods/531515.jpg", "./foods/53217.jpg", "./foods/543149.jpg", "./foods/584201.jpg", "./foods/614975.jpg", "./foods/62855.jpg", "./foods/655917.jpg", "./foods/66183.jpg", "./foods/662526.jpg", "./foods/743722.jpg", "./foods/745189.jpg", "./foods/873051.jpg", "./foods/884986.jpg", "./foods/903753.jpg", "./foods/952437.jpg", "./foods/97524.jpg", "./foods/988559.jpg", "./foods/99997.jpg"]
data = []
for image in images:
  data.append(read(image))

"""
def dist(ax, ay, bx, by):
  dx = ax - bx
  dy = ay - by
  return sqrt(dx * dx + dy * dy)
"""


def dist(a, b):
  n = 64
  # suma = sum(a)
  # sumb = sum(b)
  return sqrt(sum((a[i] - b[i])**2 for i in range(n)))


n = len(data)
m = 5

edges = []
for i in range(n):
  for j in range(i + 1, n):
    edges.append((dist(data[i], data[j]), i, j))
edges.sort()
# print(edges)

uni = UnionFind(n)
for (cost, i, j) in edges:
  if uni.group_count() == m:
    break
  if uni.size(i) > 12 or uni.size(j) > 12:
    continue
  if(uni.union(i, j)):
    print(cost, i, j)
    print(uni.all_group_members())


import subprocess
for root, group in uni.all_group_members().items():
  dir_name = "img" + str(root)
  subprocess.run(["mkdir", dir_name])
  for i in group:
    img_name = str(images[i])
    subprocess.run(["cp", img_name, "./" + dir_name])
