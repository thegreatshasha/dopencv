import cv2
import numpy as np
from skimage import measure

def check(arr, x,y):
    val = max(arr[x][y], arr[x-1][y], arr[x+1][y], arr[x][y-1], arr[x][y+1], arr[x-1][y-1], arr[x-1][y+1], arr[x+1][y-1], arr[x+1][y+1])
    if val == arr[x][y]:
      return True
    else:
      return False

# Find the region which has the same value as the current pixel and set it to 255
#
def dfsset(inp, i, j, pixel):
    #print i,j,inp[i][j]==pixel,inp[i][j]
    if not (inp[i][j] == pixel):
        return
    else:
        inp[i][j] = 0
    if j>0:
        dfsset(inp, i, j-1, pixel)
    if j<inp.shape[1]-1:
        dfsset(inp, i+1, j, pixel)
    if i>0:
        dfsset(inp, i-1, j, pixel)
    if i<inp.shape[0]-1:
        dfsset(inp, i, j+1, pixel)

# Regional maxima detection using
def regional_maxima(inp):
    out = inp.copy()

    # Iterate over all pixels
    for i in xrange(1, inp.shape[0]-1):
        for j in xrange(1, inp.shape[1]-1):
            #print i,j,inp[i][j]
            pixel = inp[i][j]

            # If this pixel has already been marked as non minima, screw this
            if out[i][j] == 0:
                continue

            # Compare pixel with its neighbours in input, if small, do dfs and set to 255
            if check(inp, i, j) == False:
                dfsset(out, i, j, pixel)

    return out

def colony_count(img_b):
    dist_transform = cv2.distanceTransform(img_b,cv2.cv.CV_DIST_L2,3)
    mx = regional_maxima(dist_transform)
    return measure.label(mx, background=0).max()

def cropContour(img, contours, idx):
    cimg = np.zeros_like(img)
    cv2.drawContours(cimg, contours, idx, color=(255,255,255), thickness=-1)
    x,y,w,h = cv2.boundingRect(contours[idx])
    return cimg[y-1:y+h+1,x-1:x+w+1]
