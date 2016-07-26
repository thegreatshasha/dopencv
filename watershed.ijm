//run("Blobs (25K)");                                  //open sample image
open("/Users/shashwat/workspace/dopencv/images/set1/B_170.jpg");
run("8-bit");
run("Auto Threshold", "method=Otsu white");
myImageID = getImageID();                            //remember the image
setTool("oval");                                          //Rectangle tool
beep();                                              //alert the user
waitForUser("Please select an roi. Click ok when done.")
run("Clear Outside");
run("Make Binary");
run("Open");
run("Watershed");
run("Analyze Particles...", "size=20-Infinity circularity=0.50-1.00 show=Outlines display summarize");
