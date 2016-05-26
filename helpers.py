import cv2

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

""" Fix image width and resize height accrdingly """
def scaled_resize(img, w_new):
    h_old = img.shape[0]
    w_old = img.shape[1]
    h_new = (h_old * w_new)/w_old
    return cv2.resize(img, (w_new, h_new))

""" Percentage difference between two values """
def perc_diff(original, new):
    return abs(float(original - new)/float(original))
