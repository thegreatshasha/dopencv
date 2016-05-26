import cv2
import cv2.cv as cv
import numpy as np

""" Preprocess image """
def preprocess(img):
    """ Resize image """
    #img = cv2.resize(img,(200, 200))
    #img2 = cv2.resize(img,(200, 200))
    # img = cv2.medianBlur(img,5)
    # img = cv2.cvtColor( img, cv2.COLOR_BGR2GRAY )
    #
    # circles = cv2.HoughCircles(img,cv.CV_HOUGH_GRADIENT,1,1,
    #                             param1=50,param2=30,minRadius=0,maxRadius=0)

""" Auto canny detection """
def auto_canny(image, sigma=0.33):
	# compute the median of the single channel pixel intensities
	v = np.median(image)

	# apply automatic Canny edge detection using the computed median
	lower = int(max(0, (1.0 - sigma) * v))
	upper = int(min(255, (1.0 + sigma) * v))
	edged = cv2.Canny(image, lower, upper)

	# return the edged image
	return edged

""" Takes an array of contours and removes the ons that differ by less than 30% from the maximum """
def get_second_largest(contours):
    cont_desc = contours.sort(key=lambda x: cv2.minEnclosingCircle(x), reverse=True)

""" Count circles in a grayscale image. Uses the contour method as of now """
def count_circles(img_g, img, canny=True):
    """ Canny binerization """
    if canny:
        img_g = auto_canny(img_g)

    """ Use either contour detection or hough transform to identify colonies """
    circs, img = hough_circles(img_g, img)

    return len(circs)

""" Detection methods """

""" Hough circles detection method"""
def hough_circles(img, img2):
    height, width = img.shape[:2]
    #img = cv2.resize(img,(width/4, height/4))
    img = cv2.GaussianBlur(img, (9, 9), 2, 2)
    #img = cv2.cvtColor( img, cv2.COLOR_BGR2GRAY )
    #img = cv2.adaptiveThreshold(img, 255, cv2.ADAPTIVE_THRESH_GAUSSIAN_C, cv2.THRESH_BINARY_INV, 11, 1)

    #circles = cv2.HoughCircles(img, cv2.cv.CV_HOUGH_GRADIENT, 2, 10, np.array([]), 20, 60, width/10)[0]
    #circles = cv2.HoughCircles(img, cv2.cv.CV_HOUGH_GRADIENT, 1.89, 5)[0]
    #circles = cv2.HoughCircles(img, cv2.cv.CV_HOUGH_GRADIENT, )
    circles = cv2.HoughCircles(img, cv2.cv.CV_HOUGH_GRADIENT,
              1.0,
              0.001,
              param1=100,
              param2=3.5,
              minRadius=2,
              maxRadius=4  )[0]
    #radii_n = circles[:,2]

    result = []

    for c in circles:
        #import pdb; pdb.set_trace()
        x, y, radius = c
        #print radius
        center = (int(x), int(y))
        radius = int(radius)
        # if radius < 0 or radius > 50:
        #     continue
        #if draw:
        cv2.circle(img2, center, radius, (0, 255, 0), 2)
        result.append([center[0], center[1], radius])
    return result, img2


""" How well does canny + contours work? """
def process_image_using_contours(img, img2):
    #img = cv2.GaussianBlur(img, (9, 9), 2, 2)
    threshold =  int(img.max() - img.std())
    rect, thresh = cv2.threshold(img, threshold, 255, 0)
    contours, hier = cv2.findContours(thresh, mode=cv2.RETR_LIST, method=cv2.CHAIN_APPROX_SIMPLE)
    #cv2.drawContours(img, contours, -1, (255, 0, 0), 3)
    circles = []
    radiis = []
    for cnt in contours:
        (x, y), radius = cv2.minEnclosingCircle(cnt)
        radiis.append(radius)
        #print radius
        center = (int(x), int(y))
        radius = int(radius)
        #if radius > 23 or radius < 2:
        #   continue
        cv2.circle(img2, center, radius, (255, 255, 0), 2)
        circles.append([center[0], center[1], radius])
    return circles, img2

""" Hough circles with deviance pruning """
def process_image_using_canny(img, img2):
    kernel = np.ones((4,4),np.uint8)
    img = cv2.erode(img,kernel,iterations = 1)
    img = cv2.dilate(img,kernel,iterations = 1)

    threshold =  int(img.max() - img.std())
    #rect, thresh = cv2.threshold(img, threshold, 255, 0)
    rect,thresh = cv2.threshold(img,0,255,cv2.THRESH_BINARY+cv2.THRESH_OTSU)
    #blur = cv2.GaussianBlur(img,(5,5),0)
    #rect, thresh = cv2.threshold(blur,0,255,cv2.THRESH_BINARY+cv2.THRESH_OTSU)
    #import pdb; pdb.set_trace()

    contours, hier = cv2.findContours(thresh,mode=cv2.RETR_LIST, method=cv2.CHAIN_APPROX_NONE)
    cv2.drawContours(img2, contours, -1, 255, 2)

    #import pdb; pdb.set_trace()

    #import pdb; pdb.set_trace()
    circles = []
    for cnt in contours:
        (x, y), radius = cv2.minEnclosingCircle(cnt)
        center = (int(x), int(y))
        radius = int(radius)
        if radius > 23 or radius < 2:
            continue
        cv2.circle(img2, center, radius, 0, 2)
        circles.append([center[0], center[1], radius])

    """ Filter and return the circles which satisfy this criteria """
    radiis = [cv2.minEnclosingCircle(cnt) for cnt in contours]


    return circles, img2
