    public void shareScreen(double projW, double projH) {
        Robot robot;
        try {
            robot = new Robot();
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Dimension screenSize = toolkit.getScreenSize();
            Rectangle screenRect = new Rectangle(screenSize);
            BufferedImage img = robot.createScreenCapture(screenRect);
            BufferedImage dst;
            double thisScrH = screenSize.height;
            double thisScrW = screenSize.width;
            double ratio = projW / projH;
            if (projW == thisScrW && projH == thisScrH) {
                dst = img;
            } else if ((ratio) == (thisScrH / thisScrW)) {
                double scaleW = projW / thisScrW;
                double scaleH = projH / thisScrH;
                BufferedImageOp op = new AffineTransformOp(AffineTransform.getScaleInstance(scaleW, scaleH), new RenderingHints(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC));
                dst = op.filter(img, null);
            } else {
                double h, w;
                if (Math.max(projW, projH) == projW) {
                    h = projW / ratio;
                    w = projW;
                } else {
                    w = projW * ratio;
                    h = projH;
                }
                double scaleW = w / thisScrW;
                double scaleH = h / thisScrH;
                BufferedImageOp op = new AffineTransformOp(AffineTransform.getScaleInstance(scaleW, scaleH), new RenderingHints(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC));
                dst = op.filter(img, null);
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                ImageIO.write(dst, "gif", baos);
            } catch (IOException e) {
                throw new IllegalStateException(e.toString());
            }
            byte[] b = baos.toByteArray();
            baos.flush();
            new RoomProjectorService().displayScreen(b, "Perumal");
            sendScreenImage(b, new PSWProjCallbackListener(projRemCtrl));
        } catch (AWTException e) {
            System.out.println("AWTException while capturing screen");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Exception while capturing screen");
            e.printStackTrace();
        }
    }
