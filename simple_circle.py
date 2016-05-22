import cv2
import cv2.cv as cv
import numpy as np

filename = 'cv_test.png'
img = cv2.imread(filename, 0) # Reads a grayscale image
img2 = cv2.imread(filename) # Read a colored image
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

""" Display an image """
def imshow(img1, message):
    screen_res = 800, 600
    scale_width = screen_res[0] / img1.shape[1]
    scale_height = screen_res[1] / img1.shape[0]
    scale = min(scale_width, scale_height)
    window_width = int(img1.shape[1] * scale)
    window_height = int(img1.shape[0] * scale)
    cv2.namedWindow(message, cv2.WINDOW_NORMAL)
    cv2.resizeWindow(message, window_width, window_height)
    cv2.imshow(message,img1)
    cv2.waitKey(0)
    cv2.destroyAllWindows()

""" Hough circles """
def hough_circles(img, img2):
    height, width = img.shape[:2]
    #img = cv2.resize(img,(width/4, height/4))
    #img = cv2.GaussianBlur(img, (9, 9), 2, 2)
    #img = cv2.cvtColor( img, cv2.COLOR_BGR2GRAY )
    #img = cv2.adaptiveThreshold(img, 255, cv2.ADAPTIVE_THRESH_GAUSSIAN_C, cv2.THRESH_BINARY_INV, 11, 1)

    #circles = cv2.HoughCircles(img, cv2.cv.CV_HOUGH_GRADIENT, 2, 10, np.array([]), 20, 60, width/10)[0]
    circles = cv2.HoughCircles(img, cv2.cv.CV_HOUGH_GRADIENT, 1.89, 5)[0]
    #import pdb; pdb.set_trace()

    result = []

    for c in circles:
        x, y, radius = c
        #print radius
        center = (int(x), int(y))
        radius = int(radius)
        #if radius > max_r or radius < min_r:
        #    continue
        #if draw:
        cv2.circle(img2, center, radius, (0, 255, 0), 2)
        result.append([center[0], center[1], radius])
    return result, img2


def process_image_using_canny(img, img2):
    #img = cv2.GaussianBlur(img, (9, 9), 2, 2)
    threshold =  int(img.max() - img.std())
    rect, thresh = cv2.threshold(img, threshold, 255, 0)
    contours, hier = cv2.findContours(thresh, mode=cv2.RETR_LIST, method=cv2.CHAIN_APPROX_SIMPLE)
    #cv2.drawContours(img, contours, -1, (255, 0, 0), 3)
    circles = []
    for cnt in contours:
        (x, y), radius = cv2.minEnclosingCircle(cnt)
        #print radius
        center = (int(x), int(y))
        radius = int(radius)
        #if radius > 23 or radius < 2:
        #   continue
        cv2.circle(img2, center, radius, (0, 255, 0), 2)
        circles.append([center[0], center[1], radius])
    return circles, thresh

#img = auto_canny(img)
circ2, img4 = hough_circles(img, img2)
print len(circ2)
#import pdb; pdb.set_trace()
imshow(img4, 'homi')
