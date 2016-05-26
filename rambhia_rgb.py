import cv2
import numpy as np
import sys

grey = cv2.imread(sys.argv[1])

h, w, _ = grey.shape

if h > 600:
	grey = cv2.resize(grey, (w/4, h/4))
else:
	grey = cv2.resize(grey, (w*8, h*8))

h, w, _ = grey.shape

rangeImg = cv2.inRange(grey, (200, 200, 200), (255, 255, 255))

cv2.imshow("grey", grey)
cv2.imshow("range", rangeImg)

# blur = cv2.blur(rangeImg, (3,3))
# rangeImg = cv2.addWeighted(rangeImg, 1.5, blur, -0.5, 0)
# cv2.imshow("blur", rangeImg)

contours, _ = cv2.findContours(rangeImg, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_NONE)
# print contours
print len(contours)
print type(contours)

finalContours = []

maxArea = w * h * 0.00018

for contour in contours:
	area = cv2.contourArea(contour)
	permieter = cv2.arcLength(contour, True)
	moments = cv2.moments(contour)

	# (x, y), radius = cv2.minEnclosingCircle(cnt)
	# center = (int(x), int(y))
	# radius = int(radius)
	# if radius > 23 or radius < 2:
	# 	continue

	if area < maxArea:
		finalContours.append(contour)
		print area, permieter
		print moments

print "Final contours", len(finalContours)

origContours = np.copy(grey)
cv2.drawContours(origContours, contours, -1, 255, 2)

cv2.drawContours(grey, finalContours, -1, 255, 2)

cv2.imshow("origContours", origContours)
cv2.imshow("contours", grey)

print len(contours)
print len(finalContours)
print w, h
print maxArea


cv2.imwrite("result.jpg", grey)

cv2.waitKey(0)
