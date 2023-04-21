    @Override
    public void mouseEntered(java.awt.event.MouseEvent evt) {
        if (!hasAni) {
            return;
        }
        trayImg.setBackgroud(robot.createScreenCapture(this.getBounds()));
        aniDir = true;
        if (!timer.isRunning()) {
            timer.start();
        }
        trayImg.enActive(evt.getPoint());
    }
