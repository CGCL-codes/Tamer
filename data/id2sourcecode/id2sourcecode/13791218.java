    private final void grabFrame() {
        BufferedImage bi = this.getImage();
        Graphics2D g2d = bi.createGraphics();
        if (this.grabSource.value.equals(PPMFrameExporter.NETSTRUCTGRAB)) {
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, this.currWidth, this.currHeight);
            for (int i = 0; i < sr.length; i++) {
                this.sr[i].paint(g2d.create(sr[i].getX(), sr[i].getY(), sr[i].getWidth(), sr[i].getHeight()));
            }
            for (int i = 0; i < nr.length; i++) {
                this.nr[i].paint(g2d.create(nr[i].getX(), nr[i].getY(), nr[i].getWidth(), nr[i].getHeight()));
            }
        } else {
            bi = this.robot.createScreenCapture(this.screen);
            Runtime.getRuntime().runFinalization();
            System.gc();
        }
        bi.flush();
        this.queuedFrames.add(bi);
    }
