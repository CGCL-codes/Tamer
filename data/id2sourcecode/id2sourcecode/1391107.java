    public synchronized BufferedImage toImage() {
        final BufferedImage capture = robot.createScreenCapture(followsMouse() ? mouseRectangle() : screenArea);
        String text = infoText.getText();
        if (text != null) {
            Graphics2D pic = capture.createGraphics();
            try {
                int height = Math.max(15, capture.getHeight() / 20);
                pic.setColor(Color.RED);
                pic.setFont(pic.getFont().deriveFont((float) (height)).deriveFont(Font.BOLD));
                pic.drawString(text, 0, height);
            } finally {
                pic.dispose();
            }
        }
        if (showMousePointer) {
            Graphics2D graphics2d = capture.createGraphics();
            int mouseX = 0, mouseY = 0;
            double heightStretch = 0, widthStretch = 0;
            Point mousePoint = MouseInfo.getPointerInfo().getLocation();
            switch(mode) {
                case FULL_SCREEN:
                    mouseX = mousePoint.x;
                    mouseY = mousePoint.y;
                    heightStretch = fullScreenArea.height;
                    widthStretch = fullScreenArea.width;
                    break;
                case FOLLOW_MOUSE:
                    mouseX = Math.min(mouseArea.width / 2, mousePoint.x);
                    mouseY = Math.min(mouseArea.height / 2, mousePoint.y);
                    if ((fullScreenArea.width - mouseArea.width / 2) < mousePoint.x) mouseX = mouseArea.width - fullScreenArea.width + mousePoint.x;
                    if ((fullScreenArea.height - mouseArea.height / 2) < mousePoint.y) mouseY = mouseArea.height - fullScreenArea.height + mousePoint.y;
                    heightStretch = mouseArea.height;
                    widthStretch = mouseArea.width;
                    break;
            }
            heightStretch = Math.max(200, heightStretch) / 200;
            widthStretch = Math.max(200, widthStretch) / 200;
            Polygon mousePointer = new Polygon();
            mousePointer.addPoint(mouseX, mouseY);
            mousePointer.addPoint(mouseX, (int) (mouseY + 17 * heightStretch));
            mousePointer.addPoint((int) (mouseX + 10 * widthStretch), (int) (mouseY + 12 * heightStretch));
            graphics2d.setColor(Color.WHITE);
            graphics2d.fill(mousePointer);
            graphics2d.setColor(Color.BLACK);
            graphics2d.draw(mousePointer);
            graphics2d.dispose();
        }
        return capture;
    }
