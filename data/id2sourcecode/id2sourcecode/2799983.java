    public void saveImage(Image image, boolean last) throws FormatException, IOException {
        if (image == null) {
            throw new FormatException("Image is null");
        }
        out = new RandomAccessFile(currentId, "rw");
        BufferedImage img = (cm == null) ? ImageTools.makeBuffered(image) : ImageTools.makeBuffered(image, cm);
        int width = img.getWidth();
        int height = img.getHeight();
        byte[][] byteData = ImageTools.getBytes(img);
        DataTools.writeString(out, "%!PS-Adobe-2.0 EPSF-1.2\n");
        DataTools.writeString(out, "%%Title: " + currentId + "\n");
        DataTools.writeString(out, "%%Creator: LOCI Bio-Formats\n");
        DataTools.writeString(out, "%%Pages: 1\n");
        DataTools.writeString(out, "%%BoundingBox: 0 0 " + width + " " + height + "\n");
        DataTools.writeString(out, "%%EndComments\n\n");
        DataTools.writeString(out, "/ld {load def} bind def\n");
        DataTools.writeString(out, "/s /stroke ld /f /fill ld /m /moveto ld /l " + "/lineto ld /c /curveto ld /rgb {255 div 3 1 roll 255 div 3 1 " + "roll 255 div 3 1 roll setrgbcolor} def\n");
        DataTools.writeString(out, "0 0 translate\n");
        DataTools.writeString(out, ((float) width) + " " + ((float) height) + " scale\n");
        DataTools.writeString(out, "/picstr 40 string def\n");
        if (byteData.length == 1) {
            DataTools.writeString(out, width + " " + height + " 8 [" + width + " 0 0 " + (-1 * height) + " 0 " + height + "] {currentfile picstr " + "readhexstring pop} image\n");
            int charCount = 0;
            for (int i = 0; i < byteData[0].length; i++) {
                for (int j = 0; j < 1; j++) {
                    String s = Integer.toHexString(byteData[j][i]);
                    if (s.length() > 1) s = s.substring(s.length() - 2); else s = "0" + s;
                    DataTools.writeString(out, s);
                    charCount++;
                    if (charCount == 40) {
                        DataTools.writeString(out, "\n");
                        charCount = 0;
                    }
                }
            }
        } else {
            DataTools.writeString(out, width + " " + height + " 8 [" + width + " 0 0 " + (-1 * height) + " 0 " + height + "] {currentfile picstr " + "readhexstring pop} false 3 colorimage\n");
            int charCount = 0;
            for (int i = 0; i < byteData[0].length; i++) {
                for (int j = 0; j < byteData.length; j++) {
                    String s = Integer.toHexString(byteData[j][i]);
                    if (s.length() > 1) s = s.substring(s.length() - 2); else s = "0" + s;
                    DataTools.writeString(out, s);
                    charCount++;
                    if (charCount == 40) {
                        DataTools.writeString(out, "\n");
                        charCount = 0;
                    }
                }
            }
        }
        DataTools.writeString(out, "showpage\n");
        out.close();
    }
