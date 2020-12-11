import cv2

img = cv2.imread('tapu.jpg', 0)

# 手動
threshold = 150
threshold_max = 255
#ret, img_thres = cv2.threshold(img, threshold, threshold_max, cv2.THRESH_BINARY)

# 自動　大津のアルゴリズム imread(name,0)のように第二引数を0にする
ret, img_thres = cv2.threshold(img, 0, 255, cv2.THRESH_OTSU)
print(ret)

cv2.imshow('tapu otsu', img_thres)
cv2.waitKey(0)
cv2.destroyAllWindows()
