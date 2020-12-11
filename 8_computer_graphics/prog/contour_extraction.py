import cv2

img = cv2.imread('tapu.jpg')
cv2.imshow('tapu',img)

gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
ret, img_thres = cv2.threshold(gray, 0, 255, cv2.THRESH_OTSU)
img_cont, contours, hierarchy = cv2.findContours(img_thres, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
contours = list(filter(lambda x: cv2.contourArea(x) > 100, contours))

cv2.drawContours(img, contours, -1, color=(0, 0, 255), thickness=2)

cv2.imshow('tapu contours', img)
cv2.waitKey(0)
cv2.destroyAllWindows()
