import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import org.bytedeco.javacv.CanvasFrame;

import org.bytedeco.javacv.OpenCVFrameConverter;

import javax.naming.OperationNotSupportedException;
import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.bytedeco.javacpp.flandmark.*;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvLoadImage;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_objdetect.*;

public class HelloWorld {

public static void main(String[] args){
IplImage image = cvLoadImage("/Users/shashwat/workspace/dopencv/images/coins.jpg");
//CanvasFrame canvas = new CanvasFrame("Btoom!", 1);
final OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
//canvas.showImage(converter.convert(image));
cvSaveImage("1.jpg", image);
}
}
