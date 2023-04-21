        public BufferedImage getImage() {
            Component comp = getComponent(0);
            Rectangle rect = new Rectangle(comp.getLocationOnScreen(), comp.getSize());
            try {
                Robot robot = new Robot(this.getGraphicsConfiguration().getDevice());
                return robot.createScreenCapture(rect);
            } catch (AWTException e) {
                e.printStackTrace();
            }
            return null;
        }
