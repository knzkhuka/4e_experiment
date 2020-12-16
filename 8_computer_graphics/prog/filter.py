import cv2
import numpy as np

img = cv2.imread('tapu.jpg')
gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)

#kernel = np.array([[-1, -1, -1], [-1, 9, -1], [-1, -1, -1]], np.float32)
kernel = np.ones((5, 5), np.float32) / 25
img_filter = cv2.filter2D(img, -1, kernel)
img_filter = cv2.filter2D(img_filter, -1, kernel)
img_filter = cv2.filter2D(img_filter, -1, kernel)
#img_filter1 = cv2.Laplacian(gray, cv2.CV_64F)
#img_filter2 = cv2.bilateralFilter(img, 15, 20, 20)
img_filter3 = cv2.Canny(img, 15, 20, 20)
img_filter4 = cv2.Canny(img_filter, 15, 20, 20)

ret, img_thres = cv2.threshold(img_filter4, 0, 255, cv2.THRESH_OTSU)
img_cont, contours, hierarchy = cv2.findContours(img_thres, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
contours = list(filter(lambda x: cv2.contourArea(x)>10, contours))
cv2.drawContours(img, contours, -1, color=(0, 0, 255), thickness=2)

#cv2.imwrite('../pic/canny1.png', img_filter3)
#cv2.imwrite('../pic/smoothing.png', img_filter)
#cv2.imwrite('../pic/canny2.png', img_filter4)

#cv2.imshow('tapu filetr', img_filter)
#cv2.imshow('tapu filetr1', img_filter1)
#cv2.imshow('tapu filetr2', img_filter2)
#cv2.imshow('tapu filetr3', img_filter3)
cv2.imshow('tapu filetr4', img_filter4)
cv2.imshow('contour', img)
cv2.waitKey(0)
cv2.destroyAllWindows()
