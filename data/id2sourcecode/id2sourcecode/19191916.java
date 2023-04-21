    public void run() {
        while (true) {
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
            }
            if (aComponent.isShowing() && aFilteringImageComponents.size() > 0) {
                Point lLocation;
                int lMaxXPosition, lMaxYPosition;
                Rectangle lRectangle;
                BufferedImage lImage;
                lLocation = MouseInfo.getPointerInfo().getLocation();
                lLocation.x -= aWidth / 2;
                lLocation.y -= aHeight / 2;
                lMaxXPosition = aScreenSize.width - aWidth;
                if (lLocation.x < 0) {
                    lLocation.x = 0;
                } else if (lLocation.x > lMaxXPosition) {
                    lLocation.x = lMaxXPosition;
                }
                lMaxYPosition = aScreenSize.height - aHeight;
                if (lLocation.y < 0) {
                    lLocation.y = 0;
                } else if (lLocation.y > lMaxYPosition) {
                    lLocation.y = lMaxYPosition;
                }
                lRectangle = new Rectangle(lLocation.x, lLocation.y, aWidth, aHeight);
                lImage = aRobot.createScreenCapture(lRectangle);
                for (FilteringImageComponent lComponent : aFilteringImageComponents) {
                    lComponent.setImage(lImage);
                }
            }
        }
    }
