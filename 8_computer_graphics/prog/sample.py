import cv2

# 画像の読み込み
img = cv2.imread('lena.jpg')

# 画像のグレースケール化
gray = cv2.cvtColor(img, cv2.COLOR_RGB2GRAY)

# cv2.imshowによる画像の表示
cv2.imshow('lena_gray', gray)
cv2.waitKey(0)
cv2.destroyAllWindows()

# 画像の書き出し
cv2.imwrite('lena_gray.jpg', gray)
