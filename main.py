import numpy as np
import cv2
import argparse
from detect import process_image_using_contours, auto_canny, hough_circles, process_image_using_canny
from helpers import imshow, scaled_resize

""" Build the argument parser """
ap = argparse.ArgumentParser()
ap.add_argument("-i", "--image", required = True, help = "Path to the image")
args = vars(ap.parse_args())

filename = args['image']

""" Read the input file and resize the image"""
img = cv2.imread(filename, 0) # Reads a grayscale image
img2 = cv2.imread(filename) # Read a colored image
h, w = img.shape

#img = cv2.resize(img,(300, 300)) This is skewing the ratios
#img2 = cv2.resize(img,(300, 300))
img = scaled_resize(img, 425)
img2 = scaled_resize(img2, 425)

# if h>600:
#   img = cv2.resize(img, (w/4, h/4))
#   img2 = cv2.resize(img2, (w/4, h/4))
# else:
#   img = cv2.resize(img, (w/4, h/4))
#   img2 = cv2.resize(img2, (w/4, h/4))

""" Find max contour and find points inside """
#def max_contours(img, img2:)

""" Apply canny edge detection to image first """
#img = auto_canny(img)

""" Use either contour detection or hough transform to identify colonies """
#circ2, img4 = process_image_using_canny(img, img2)
#circ2, img4 = process_image_using_contours(img, img2)
circ2, img4 = hough_circles(img, img2)
#import pdb; pdb.set_trace()

print len(circ2)
#import pdb; pdb.set_trace()
imshow(np.hstack([img4]), 'homi')
#import pdb; pdb.set_trace()
