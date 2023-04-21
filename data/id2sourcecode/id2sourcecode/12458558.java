    public static JaguarImage createJaguarImage(JaguarRectangle r) {
        robby.delay(20);
        robby.waitForIdle();
        return new JaguarImage(robby.createScreenCapture(r));
    }
