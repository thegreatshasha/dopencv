import cv2
import numpy as np
import cv2.cv as cv

img = cv2.imread('detect_circles_8circles.jpg')
height, width = img.shape[:2]
#img = cv2.resize(img,(width/4, height/4))
img = cv2.GaussianBlur(img, (9, 9), 2, 2)
img = cv2.cvtColor( img, cv2.COLOR_BGR2GRAY )
img = cv2.adaptiveThreshold(img, 255, cv2.ADAPTIVE_THRESH_GAUSSIAN_C,
    cv2.THRESH_BINARY_INV, 11, 1)
circles = cv2.HoughCircles(img, cv2.cv.CV_HOUGH_GRADIENT, 2, 10, np.array([]), 20, 60, width/10)[0]

#kernel = np.ones((3, 3), np.uint8)
#img = cv2.morphologyEx(img, cv2.MORPH_CLOSE, kernel, iterations=4)
#
#contours, hierarchy = cv2.findContours(img, cv2.RETR_EXTERNAL,
#    cv2.CHAIN_APPROX_SIMPLE)

#cv2.drawContours(img, contours, -1, (0,255,0), 3)

#print len(contours)

# img = cv2.cvtColor( img, cv2.COLOR_BGR2GRAY )
#
# img = cv2.threshold( img, 0, 255,cv2.THRESH_BINARY + cv2.THRESH_OTSU)[1]
#
# #img = cv2.medianBlur(img,5)
# cimg = cv2.cvtColor(img,cv2.COLOR_GRAY2BGR)
# #
circles = cv2.HoughCircles(img, cv2.cv.CV_HOUGH_GRADIENT, 0.2, 100)
print circles
#
# circles = np.uint16(np.around(circles))
# for i in circles[0,:]:
#     # draw the outer circle
#     #cv2.circle(cimg,(i[0],i[1]),i[2],(0,255,0),2)
#     # draw the center of the circle
#     cv2.circle(img,(i[0],i[1]),2,(0,0,255),3)

cv2.imshow('detected circles',img)
cv2.waitKey(0)
cv2.destroyAllWindows()
