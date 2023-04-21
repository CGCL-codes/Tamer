    private boolean isEquealed(BufferedImage oldBI) {
        BufferedImage newBI = robot.createScreenCapture(this.rectangle);
        return Util.compareBufferedImage(oldBI, newBI);
    }
