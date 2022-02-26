import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Image_to_ASCII extends PApplet {

final String s1 = "            .,:-;_~*!/+1=7c?4a32b50698#$ÑW@";
final String s2 = "       .:-i|=+%O#W@";
final String density = s1;
final int w = 8;
PImage img= null;
String savePath= "";
PrintWriter writer = null;
int t= 0;
int state= 0;
public void setup() {
  
  rectMode(CENTER);
  textAlign(CENTER, CENTER);
  noStroke();
  welcomeScreen();
}
public void welcomeScreen(){
  background(0);
  fill(0xff56F554);
  textSize(30);
  text("Press O to open\nPress S to save as png\nPress T to save as txt", width/2, height/2, width/2, 300);
}
public void draw(){
  if(img==null) return;
  background(0xff56F554);
  pushMatrix();
  translate(width/2, height/2);
  fill(0);
  rect(0, 0, img.width*w, img.height*w);
  textSize(w*.9f);
  fill(0xff56F554);
  img.loadPixels();
  for (int x=0; x<img.width; x++) {
    for (int y=0; y<img.height; y++) {
      int i= PApplet.parseInt(brightness(img.pixels[x + y * img.width]) * (density.length()-1)/255.0f);
      // println(i, density.length());
      String s= str(density.charAt(i));
      text(s, x*w + w/2 - img.width*w/2, y*w + w/2 - img.height*w/2, w*1.4f, w*1.4f);
    }
  }
  popMatrix();
  if(savePath!=""){
    save(savePath);
    savePath= "";
    state= 1;
  }
  if(state!=0){
    textSize(30);
    fill(0);
    rect(width/2, 100, width/2, 50);
    fill(0xff56F554);
    if(state==1){
      text("Saved Successfully", width/2, 100, width/2, 50);
      t++;
      if(t>frameRate*3){
        t= 0;
        state= 0;
      }
    }
  }
}
public void openImage(){
  selectInput("Select a file to process:", "imgSelected");
}
public void saveImageAsPNG(){
  if(img==null) return;
  selectOutput("Select a folder to process:", "pngFileSelected", new File("ASCII.png"));
}
public void saveImageAsTXT(){
  if(img==null) return;
  selectOutput("Select a folder to process:", "txtFileSelected", new File("ASCII.txt"));
}
public void fitImage(int w, int h) {
  int i_w= img.width, i_h = img.height;
  float w_s = (float) i_w/w;
  float h_s = (float) i_h/h;
  int in_w= w, in_h= h;
  if (w_s < h_s) in_w = PApplet.parseInt(i_w/h_s);
  else in_h = PApplet.parseInt(i_h/w_s);
  img.resize(in_w, in_h);
}
public void keyPressed(){
  if(key=='o' || key=='O') thread("openImage");
  else if(key=='s' || key=='S') thread("saveImageAsPNG");
  else if(key=='t' || key=='T') thread("saveImageAsTXT");
}
public String replace(String str, String a, String b) {
  String s= "";
  String[] arr= split(str, a);
  for (int i=0; i<arr.length; i++) {
    s+=arr[i];
    if (i!=arr.length-1) s+=b;
  }
  return s;
}
public void pngFileSelected(File selection) {
  if (selection == null) {
    return;
  } else {
    savePath= replace(selection.getAbsolutePath(), "\\", "/");
  }
}
public void txtFileSelected(File selection) {
  if (selection == null) {
    return;
  } else {
    savePath= replace(selection.getAbsolutePath(), "\\", "/");
  }
  writer= createWriter(savePath);
  savePath= "";
  for (int y=0; y<img.height; y++) {
    for (int x=0; x<img.width; x++) {
      int i= PApplet.parseInt(brightness(img.pixels[x + y * img.width]) * (density.length()-1)/255.0f);
      String s= str(density.charAt(i));
      if(s=="") writer.print("&nbsp");
      else writer.print(s);
    }
    writer.print("\n");
  }
  writer.flush();
  writer.close();
  state= 1;
}
public void imgSelected(File selection) {
  String path;
  if (selection == null) {
    return;
  } else {
    path= replace(selection.getAbsolutePath(), "/", "\\");
  }
  img= loadImage(path);
  fitImage(width, height);
  img.resize(img.width/w, img.height/w);
}
  public void settings() {  fullScreen(P2D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Image_to_ASCII" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
