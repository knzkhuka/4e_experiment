import cv2
import numpy as np

img = cv2.imread('tapu.jpg')
gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)

#kernel = np.array([[-1, -1, -1], [-1, 9, -1], [-1, -1, -1]], np.float32)
kernel = np.ones((5, 5), np.float32) / 25
img_filter = cv2.filter2D(img, -1, kernel)
#img_filter1 = cv2.Laplacian(gray, cv2.CV_64F)
#img_filter2 = cv2.bilateralFilter(img, 15, 20, 20)
img_filter3 = cv2.Canny(gray, 15, 20, 20)
img_filter4 = cv2.Canny(img_filter, 15, 20, 20)

cv2.imshow('tapu filetr', img_filter)
#cv2.imshow('tapu filetr1', img_filter1)
#cv2.imshow('tapu filetr2', img_filter2)
cv2.imshow('tapu filetr3', img_filter3)
cv2.imshow('tapu filetr4', img_filter4)
cv2.waitKey(0)
cv2.destroyAllWindows()
