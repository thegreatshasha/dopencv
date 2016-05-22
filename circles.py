import cv2
import numpy as np
import cv2.cv as cv

img = cv2.imread('image.jpg')
img = cv2.GaussianBlur(img, (25, 25), 0)
img = cv2.cvtColor( img, cv2.COLOR_BGR2GRAY )
img = cv2.adaptiveThreshold(img, 255, cv2.ADAPTIVE_THRESH_GAUSSIAN_C,
    cv2.THRESH_BINARY_INV, 11, 1)

kernel = np.ones((3, 3), np.uint8)
closing = cv2.morphologyEx(img, cv2.MORPH_CLOSE, kernel, iterations=4)

contours, hierarchy = cv2.findContours(closing, cv2.RETR_EXTERNAL,
    cv2.CHAIN_APPROX_SIMPLE)

print len(contours)

# img = cv2.cvtColor( img, cv2.COLOR_BGR2GRAY )
#
# img = cv2.threshold( img, 0, 255,cv2.THRESH_BINARY + cv2.THRESH_OTSU)[1]
#
# img = cv2.medianBlur(img,5)
# #cimg = cv2.cvtColor(img,cv2.COLOR_GRAY2BGR)
#
# circles = cv2.HoughCircles(img,cv.CV_HOUGH_GRADIENT,1,20,
#                             param1=50,param2=30,minRadius=0,maxRadius=0)
#
# print len(circles[0,:])
#
# circles = np.uint16(np.around(circles))
# for i in circles[0,:]:
#     # draw the outer circle
#     #cv2.circle(cimg,(i[0],i[1]),i[2],(0,255,0),2)
#     # draw the center of the circle
#     cv2.circle(img,(i[0],i[1]),2,(0,0,255),3)

cv2.imshow('detected circles',closing)
cv2.waitKey(0)
cv2.destroyAllWindows()
