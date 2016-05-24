""" Unit tests """
import unittest
import cv2
from detect import count_circles
from helpers import perc_diff

class TestColonyCounter(unittest.TestCase):

    """ Get it working for a single test case initially within a tolerance threshold of 15% """
    def setUp(self):
        self.imgs = ['images/image_359.jpg', 'images/cv_9.png']
        self.img_matr_g = [cv2.imread(f, 0) for f in self.imgs]
        self.img_matr = [cv2.imread(f) for f in self.imgs] # Inefficient, don't do unnecessary file reads """
        self.counts = [name.split('_')[1] for name in self.imgs]

    """ Assert that the deviation is less than 50% """
    def test_images(self):
        for name, img_g, img, true_count in zip(self.imgs, self.img_matr_g, self.img_matr, self.counts):
            ccount = count_circles(img_g, img, canny=False)
            true_count = 359
            deviation = perc_diff(true_count, ccount)
            print "%s: %f accuracy"%(name, (1.0-deviation)*100)
            self.assertLess(deviation, 0.5)

if __name__ == '__main__':
    unittest.main()
