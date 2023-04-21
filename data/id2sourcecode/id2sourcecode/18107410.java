            public Object construct() {
                try {
                    Thread.sleep(1000);
                    Rectangle area = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
                    return robot.createScreenCapture(area);
                } catch (Throwable e) {
                    Log.error(e);
                    if (mainWindowVisible) {
                        mainWindow.setVisible(true);
                    }
                    if (chatFrameVisible) {
                        chatFrame.setVisible(true);
                    }
                }
                return null;
            }
