    public static void reduceImg(String imgsrc, String imgdist, int widthdist, int heightdist, int benchmark) {
        try {
            File srcfile = new File(imgsrc);
            if (!srcfile.exists()) {
                return;
            }
            Image src = javax.imageio.ImageIO.read(srcfile);
            int width = src.getWidth(null);
            int height = src.getHeight(null);
            if (width <= widthdist && height <= heightdist) {
                FileUtils.copyFile(new File(imgsrc), new File(imgdist));
                return;
            }
            float wh = (float) width / (float) height;
            if (benchmark == 0) {
                if (wh > 1) {
                    float tmp_heigth = (float) widthdist / wh;
                    BufferedImage tag = new BufferedImage(widthdist, (int) tmp_heigth, BufferedImage.TYPE_INT_RGB);
                    tag.getGraphics().drawImage(src, 0, 0, widthdist, (int) tmp_heigth, null);
                    FileOutputStream out = new FileOutputStream(imgdist);
                    JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
                    encoder.encode(tag);
                    out.close();
                } else {
                    float tmp_width = (float) heightdist * wh;
                    BufferedImage tag = new BufferedImage((int) tmp_width, heightdist, BufferedImage.TYPE_INT_RGB);
                    tag.getGraphics().drawImage(src, 0, 0, (int) tmp_width, heightdist, null);
                    FileOutputStream out = new FileOutputStream(imgdist);
                    JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
                    encoder.encode(tag);
                    out.close();
                }
            }
            if (benchmark == 1) {
                float tmp_heigth = (float) widthdist / wh;
                BufferedImage tag = new BufferedImage(widthdist, (int) tmp_heigth, BufferedImage.TYPE_INT_RGB);
                tag.getGraphics().drawImage(src, 0, 0, widthdist, (int) tmp_heigth, null);
                FileOutputStream out = new FileOutputStream(imgdist);
                JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
                encoder.encode(tag);
                out.close();
            }
            if (benchmark == 2) {
                float tmp_width = (float) heightdist * wh;
                BufferedImage tag = new BufferedImage((int) tmp_width, heightdist, BufferedImage.TYPE_INT_RGB);
                tag.getGraphics().drawImage(src, 0, 0, (int) tmp_width, heightdist, null);
                FileOutputStream out = new FileOutputStream(imgdist);
                JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
                encoder.encode(tag);
                out.close();
            }
        } catch (IOException ex) {
            logger.error(ex);
        }
    }
