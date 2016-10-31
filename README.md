# Running

rm dataset/.DS_Store; javac -classpath /usr/local/Cellar/opencv/2.4.12_2/share/OpenCV/java/opencv-2412.jar HelloCv.java Test.java Node.java HelloDistance.java; java -cp /usr/local/Cellar/opencv/2.4.12_2/share/OpenCV/java/opencv-2412.jar:. -Djava.library.path=/usr/local/Cellar/opencv/2.4.12_2/share/OpenCV/java/ HelloDistance

* Write a unit test to check accuracy of each algorithm

* Make it require minimum no of tweaks in configurable parameters

* (Optional) Use canny edge detection to binarize image. Compare results before and after.

* Use hough transforms to find all radiis

* Convert to normal form

* Filter out radiis with deviation > threshold. How to decide threshold? No tuning required there

* Discard all circles lying outside median. Smallest circles are also the most frequent for bacterial colonies """

* Calculate mode radii of circles. Try plotting histogram of values """

* Write unit tests. Assert that accuracy for method is greater than 85% in each case """

* Write this version with unit tests first """

* Read papers on automatically counting bacterial colonies. They should give you an idea of state of the art implementation """
