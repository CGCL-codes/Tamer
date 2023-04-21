    public void addPicture() throws IOException {
        if (out == null) {
            this.start(bounds);
        }
        out.writeFrame(robot.createScreenCapture(bounds));
    }
