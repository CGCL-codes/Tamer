    public void reload() {
        try {
            Robot robot = new Robot();
            BufferedImage im = robot.createScreenCapture(new Rectangle(this.getLocationOnScreen().x, this.getLocationOnScreen().y, this.getWidth(), this.getHeight()));
            ImageSelection imgSel = new ImageSelection(im);
            clipboard.setContents(imgSel, null);
            System.out.println("Captura completa");
        } catch (AWTException e) {
            e.printStackTrace();
        }
        if (centralPanel instanceof BasicPrimitivePanel) {
            ((BasicPrimitivePanel) centralPanel).reload();
        } else if (centralPanel instanceof BasicTabContainer) {
            ((BasicTabContainer) centralPanel).reload();
        }
    }
