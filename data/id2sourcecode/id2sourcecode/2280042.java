        public void dissolveExit() {
            try {
                Robot robot = new Robot();
                Rectangle frameRect = AppFrame.this.getBounds();
                mFrameBuffer = robot.createScreenCapture(frameRect);
                AppFrame.this.setVisible(false);
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                Rectangle screenRect = new Rectangle(0, 0, screenSize.width, screenSize.height);
                mScreenBuffer = robot.createScreenCapture(screenRect);
                mFullScreen = new Window(new JFrame("Exiting BASE... Goodbye!"));
                mFullScreen.setSize(screenSize);
                mFullScreen.add(this);
                this.setSize(screenSize);
                mFullScreen.setVisible(true);
                new Thread(this).start();
            } catch (AWTException awt) {
                System.err.println("Exiting BASE: " + awt.getMessage());
                System.exit(0);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
