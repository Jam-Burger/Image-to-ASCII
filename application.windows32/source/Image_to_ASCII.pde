final String s1 = "            .,:-;_~*!/+1=7c?4a32b50698#$ÑW@";
final String s2 = "       .:-i|=+%O#W@";
final String density = s1;
final int w = 8;
PImage img= null;
String savePath= "";
PrintWriter writer = null;
int t= 0;
int state= 0;
void setup() {
  fullScreen(P2D);
  rectMode(CENTER);
  textAlign(CENTER, CENTER);
  noStroke();
  welcomeScreen();
}
void welcomeScreen(){
  background(0);
  fill(#56F554);
  textSize(30);
  text("Press O to open\nPress S to save as png\nPress T to save as txt", width/2, height/2, width/2, 300);
}
void draw(){
  if(img==null) return;
  background(#56F554);
  pushMatrix();
  translate(width/2, height/2);
  fill(0);
  rect(0, 0, img.width*w, img.height*w);
  textSize(w*.9);
  fill(#56F554);
  img.loadPixels();
  for (int x=0; x<img.width; x++) {
    for (int y=0; y<img.height; y++) {
      int i= int(brightness(img.pixels[x + y * img.width]) * (density.length()-1)/255.0);
      // println(i, density.length());
      String s= str(density.charAt(i));
      text(s, x*w + w/2 - img.width*w/2, y*w + w/2 - img.height*w/2, w*1.4, w*1.4);
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
    fill(#56F554);
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
void openImage(){
  selectInput("Select a file to process:", "imgSelected");
}
void saveImageAsPNG(){
  if(img==null) return;
  selectOutput("Select a folder to process:", "pngFileSelected", new File("ASCII.png"));
}
void saveImageAsTXT(){
  if(img==null) return;
  selectOutput("Select a folder to process:", "txtFileSelected", new File("ASCII.txt"));
}
void fitImage(int w, int h) {
  int i_w= img.width, i_h = img.height;
  float w_s = (float) i_w/w;
  float h_s = (float) i_h/h;
  int in_w= w, in_h= h;
  if (w_s < h_s) in_w = int(i_w/h_s);
  else in_h = int(i_h/w_s);
  img.resize(in_w, in_h);
}
void keyPressed(){
  if(key=='o' || key=='O') thread("openImage");
  else if(key=='s' || key=='S') thread("saveImageAsPNG");
  else if(key=='t' || key=='T') thread("saveImageAsTXT");
}
String replace(String str, String a, String b) {
  String s= "";
  String[] arr= split(str, a);
  for (int i=0; i<arr.length; i++) {
    s+=arr[i];
    if (i!=arr.length-1) s+=b;
  }
  return s;
}
void pngFileSelected(File selection) {
  if (selection == null) {
    return;
  } else {
    savePath= replace(selection.getAbsolutePath(), "\\", "/");
  }
}
void txtFileSelected(File selection) {
  if (selection == null) {
    return;
  } else {
    savePath= replace(selection.getAbsolutePath(), "\\", "/");
  }
  writer= createWriter(savePath);
  savePath= "";
  for (int y=0; y<img.height; y++) {
    for (int x=0; x<img.width; x++) {
      int i= int(brightness(img.pixels[x + y * img.width]) * (density.length()-1)/255.0);
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
void imgSelected(File selection) {
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
