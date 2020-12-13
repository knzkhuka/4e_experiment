import cv2

img = cv2.imread('tapu.jpg')
mask = cv2.imread('black.jpg')

mask = cv2.resize(mask, img.shape[1::-1])
masked_img = cv2.bitwise_and(img, mask)
#cv2.imwrite('../pic/tapu_icon.jpg', masked_img)
cv2.imshow('tapu filetr', masked_img)
cv2.waitKey(0)
cv2.destroyAllWindows()
