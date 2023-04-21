        private void update() {
            BufferedImage image = null;
            try {
                Toolkit toolkit = Toolkit.getDefaultToolkit();
                Dimension screenSize = toolkit.getScreenSize();
                Rectangle screenRect = new Rectangle(screenSize);
                Robot robot = new Robot();
                image = robot.createScreenCapture(screenRect);
            } catch (Exception ex) {
                Tools.logException(Desktop.class, ex);
            }
            try {
                setPainting(false);
                if (image != null && getApp().getContext() != null) {
                    if (image.getWidth() > mApp.getWidth() || image.getHeight() > mApp.getHeight()) {
                        BufferedImage scaled = ImageManipulator.getScaledImage(image, mApp.getWidth(), mApp.getHeight());
                        image.flush();
                        image = null;
                        getNormal().setResource(createImage(scaled));
                        scaled.flush();
                        scaled = null;
                    } else {
                        getNormal().setResource(createImage(image), RSRC_IMAGE_BESTFIT);
                        image.flush();
                        image = null;
                    }
                }
            } catch (Exception ex) {
                Tools.logException(Desktop.class, ex);
            } finally {
                setPainting(true);
            }
            getNormal().flush();
        }
