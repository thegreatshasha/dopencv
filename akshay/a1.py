import sys
sys.path.insert(0, '..')
from helpers import *
import cv2
import numpy as np
import argparse

# construct the argument parser and parse the arguments
ap = argparse.ArgumentParser()
ap.add_argument("-i", "--image", required = True, help = "Path to the image")
args = vars(ap.parse_args())

#loaded image
image=cv2.imread(args["image"])
image=image[:,:,:]
output=image.copy()
gray=cv2.cvtColor(image,cv2.COLOR_BGR2GRAY)
#cv2.imshow("",gray)
k_size=(11,11)
gray=cv2.GaussianBlur(gray,k_size,0)

gray=cv2.Canny(gray,120,40)
cv2.imshow(" ",gray)


circles = cv2.HoughCircles(gray,cv2.HOUGH_GRADIENT, 8, 5,minRadius=5,maxRadius=22 )
circles1 = cv2.HoughCircles(gray,cv2.HOUGH_GRADIENT, 2.5, 5,minRadius=5,maxRadius=22 )
circles2 = cv2.HoughCircles(gray,cv2.HOUGH_GRADIENT, 6, 5,minRadius=5,maxRadius=22 )
#print np.shape(circles)[1]
print np.shape(circles1)[1]
print np.shape(circles)[1]
print np.shape(circles2)[1]
import pdb
pdb.set_trace()

# ensure at least some circles were found
if circles is not None and circles1 is not None and circles2 is not None:
	# convert the (x, y) coordinates and radius of the circles to integers
	circles = np.round(circles[0, :]).astype("int")
 	circles1 = np.round(circles1[0, :]).astype("int")
 	circles2 = np.round(circles2[0, :]).astype("int")
	# loop over the (x, y) coordinates and radius of the circles
	for (x, y, r) in circles:
		# draw the circle in the output image, then draw a rectangle
		# corresponding to the center of the circle
		cv2.circle(output, (x, y), r, (0, 255, 0), 4)
		cv2.rectangle(output, (x - 5, y - 5), (x + 5, y + 5), (0, 128, 255), -1)
 	for (x, y, r) in circles1:
		# draw the circle in the output image, then draw a rectangle
		# corresponding to the center of the circle
		cv2.circle(output, (x, y), r, (255, 0, 0), 4)
		cv2.rectangle(output, (x - 5, y - 5), (x + 5, y + 5), (0, 128, 255), -1)
 	for (x, y, r) in circles2:
		# draw the circle in the output image, then draw a rectangle
		# corresponding to the center of the circle
		cv2.circle(output, (x, y), r, (0, 0, 255), 4)
		cv2.rectangle(output, (x - 5, y - 5), (x + 5, y + 5), (0, 128, 255), -1)
 
	# show the output image
	cv2.imshow("output", np.hstack([image, output]))
	
# detect circles in the image

print circles
cv2.waitKey(0)
