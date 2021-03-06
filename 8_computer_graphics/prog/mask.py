import cv2
import numpy as np

img = cv2.imread('tapu.jpg')
img_cpy = img.copy()
img_cpy2 = img.copy()
img_cpy3 = img.copy()

gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
ret, img_thres = cv2.threshold(gray, 180, 255, cv2.THRESH_BINARY)
img_cont, contours, hierarchy = cv2.findContours(img_thres, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
n_contours = list(filter(lambda x: cv2.contourArea(x) > 100000, contours))

cv2.drawContours(img_cpy, n_contours, -1, color=(0, 0, 0), thickness=-1)
cv2.drawContours(img_cpy2, contours, -1, color=(0, 0, 255), thickness=2)
cv2.drawContours(img_cpy3, n_contours, -1, color=(0, 255, 0), thickness=3)

black_min = np.array([0, 0, 0], np.uint8)
black_max = np.array([1, 1, 1], np.uint8)
mask = cv2.inRange(img_cpy, black_min, black_max)
mask = cv2.bitwise_not(mask)
mask_rgb = cv2.cvtColor(mask, cv2.COLOR_GRAY2RGB)

img_cpy = cv2.bitwise_and(img_cpy, mask_rgb)

cv2.imshow('mask', mask)
cv2.imshow('tapu contour1', img_cpy2)
cv2.imshow('tapu contour2', img_cpy3)
cv2.imshow('tapu blackback', img_cpy)
cv2.waitKey(0)
cv2.destroyAllWindows()

#cv2.imwrite('../pic/contour_large.png', img_cpy3)
#cv2.imwrite('../pic/silhouette.png', mask)
#cv2.imwrite('../pic/tapu_bb.png', img_cpy)

"""
hsv = cv2.cvtColor(img_cpy, cv2.COLOR_BGR2HSV_FULL)
h = hsv[:, :, 0]
s = hsv[:, :, 1]
mask = np.zeros(h.shape, dtype=np.uint8)
mask[((h < 20) | (h > 200)) & (s > 128)] = 255
print(type(mask), mask.shape)
print(type(img_cpy), img_cpy.shape)
cv2.imshow('red', mask)

mask = cv2.resize(mask, img.shape[1::-1])
gray_mask = cv2.cvtColor(mask, cv2.COLOR_BAYER_BG2GRAY)

masked_img = cv2.bitwise_and(img, gray_mask)
cv2.imshow('masked', masked_img)

red_min = np.array([0, 0, 0], np.uint8)
red_max = np.array([0, 0, 255], np.uint8)

mask_red = cv2.inRange(img_filter, red_min, red_max)
mask = cv2.bitwise_not(mask_red)
mask_rgb = cv2.cvtColor(mask, cv2.COLOR_GRAY2RGB)
masked_img = cv2.bitwise_and(img, mask_rgb)
#cv2.imshow('tapu red', mask_red)
#cv2.imshow('tapu mask', mask)
cv2.imshow('tapu filetr', masked_img)
"""
