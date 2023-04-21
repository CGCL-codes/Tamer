    protected void snapshot() {
        try {
            Robot robot = new Robot();
            Rectangle rectangle = new Rectangle(IJ.getScreenSize());
            snapshotFrame.hide();
            Image image = robot.createScreenCapture(rectangle);
            snapshotFrame.show();
            if (image != null) {
                String name = getSnapshotName();
                ImagePlus imp = new ImagePlus(name, image);
                imp.show();
                if (putSnapshotsToBack) imp.getWindow().toBack();
                TextArea area = editor.getTextArea();
                area.insert("[[Image:" + name + "]]\n", area.getCaretPosition());
            }
        } catch (AWTException e) {
        }
    }
