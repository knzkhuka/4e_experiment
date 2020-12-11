import cv2
import numpy as np

img = cv2.imread('tapu.jpg')

#kernel = np.array([[-1, -1, -1], [-1, 9, -1], [-1, -1, -1]], np.float32)
kernel = np.ones((5, 5), np.float32) / 25
img_filter = cv2.filter2D(img, -1, kernel)

cv2.imshow('tapu filetr', img_filter)
cv2.waitKey(0)
cv2.destroyAllWindows()
