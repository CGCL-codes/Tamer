                    public void run() {
                        try {
                            BufferedImage image = new Robot().createScreenCapture(bounds);
                            TokenTool.getFrame().getTokenCompositionPanel().setToken(image);
                        } catch (AWTException ae) {
                            ae.printStackTrace();
                        }
                    }
