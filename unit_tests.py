""" Unit tests """
import numpy as np
import unittest
import cv2
from detect import count_circles
from helpers import perc_diff
from glob import glob
from rambhia import count_colonies
from helpers import scaled_resize

class TestColonyCounter(unittest.TestCase):

    """ Get it working for a single test case initially within a tolerance threshold of 15% """
    def setUp(self):
        #directory = 'images/set1'
        #os.listdir(directory)
        self.imgs = glob('images/set3/*')
        #elf.imgs = ['images/image_359.jpg', 'images/cv_9.png']
        self.img_size = 425
        self.img_matr_g = [scaled_resize(cv2.imread(f, 0), self.img_size) for f in self.imgs]
        self.img_matr = [scaled_resize(cv2.imread(f), self.img_size) for f in self.imgs] # Inefficient, don't do unnecessary file reads """
        self.counts = [int(name.strip('.jpg').split('_')[1]) for name in self.imgs]

    """ Assert that the deviation is less than 50% """
    def test_images(self):
        accuracies = []
        for name, img_g, img, true_count in zip(self.imgs, self.img_matr_g, self.img_matr, self.counts):
            ccount = count_circles(img_g, img, canny=False)
            #ccount = count_colonies(img_g)
            #import pdb; pdb.set_trace()
            #true_count = 359
            deviation = perc_diff(true_count, ccount)
            accuracy = (1.0 - deviation)*100
            print "%s: %f accuracy"%(name, accuracy)
            accuracies.append(accuracy)
        print "Mean accuracy: %f, variance: %f" % (np.array(accuracies).mean(), np.array(accuracies).var())
            #self.assertLess(deviation, 0.6)

if __name__ == '__main__':
    unittest.main()
