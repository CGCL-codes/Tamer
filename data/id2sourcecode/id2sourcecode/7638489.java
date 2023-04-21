    @Override
    protected void packVideoPacket(VideoPacket $videoPacket) {
        $videoPacket.pack(System.nanoTime(), _robot.createScreenCapture(SCREEN_RECTANGLE));
    }
