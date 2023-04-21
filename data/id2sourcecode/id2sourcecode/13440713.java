    public byte[] bufferImage() {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Rectangle screenRectangle = new Rectangle(VirtualScreenBean.vScreenSpinnerX, VirtualScreenBean.vScreenSpinnerY, VirtualScreenBean.vScreenSpinnerWidth, VirtualScreenBean.vScreenSpinnerHeight);
            Robot robot = VirtualScreenBean.robot;
            if (robot == null) robot = new Robot();
            BufferedImage imageScreen = robot.createScreenCapture(screenRectangle);
            double width = imageScreen.getWidth();
            double height = imageScreen.getHeight();
            Date endTime = new Date();
            long timeInSeconds = (endTime.getTime() - this.startDate.getTime()) / 1000;
            System.out.println("1 buffer start , end , delta " + this.startDate + " " + endTime + " :timeInSeconds: " + timeInSeconds);
            double thumbWidth = 600;
            double thumbHeight = 600;
            BufferedImage image = null;
            Image img = null;
            double div = width / thumbWidth;
            System.out.println(" height:" + height + " width: " + width);
            height = height / div;
            System.out.println("div: " + div + " newheight:" + height);
            if (height > thumbHeight) {
                double divHeight = height / thumbHeight;
                thumbWidth = thumbWidth / divHeight;
                height = thumbHeight;
            }
            System.out.println("final height:" + height + " width: " + thumbWidth);
            img = imageScreen.getScaledInstance(Double.valueOf(thumbWidth).intValue(), Double.valueOf(height).intValue(), Image.SCALE_SMOOTH);
            image = new BufferedImage(Double.valueOf(thumbWidth).intValue(), Double.valueOf(height).intValue(), BufferedImage.TYPE_INT_RGB);
            Graphics2D biContext = image.createGraphics();
            biContext.drawImage(img, 0, 0, null);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            JPEGEncodeParam encpar = encoder.getDefaultJPEGEncodeParam(image);
            encpar.setQuality(ConnectionBean.imgQuality, false);
            encoder.setJPEGEncodeParam(encpar);
            encoder.encode(image);
            imageScreen.flush();
            image.flush();
            if (img != null) img.flush();
            return out.toByteArray();
        } catch (FileNotFoundException e) {
            System.out.println(e);
            e.printStackTrace();
        } catch (IOException ioe) {
            System.out.println(ioe);
            ioe.printStackTrace();
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
        return null;
    }
