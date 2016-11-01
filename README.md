#Reports
https://rawgit.com/thegreatshasha/dopencv/master/java/report.html

# Running

rm dataset/.DS_Store; javac -classpath /usr/local/Cellar/opencv/2.4.12_2/share/OpenCV/java/opencv-2412.jar HelloCv.java Test.java Node.java HelloDistance.java; java -cp /usr/local/Cellar/opencv/2.4.12_2/share/OpenCV/java/opencv-2412.jar:. -Djava.library.path=/usr/local/Cellar/opencv/2.4.12_2/share/OpenCV/java/ HelloDistance

