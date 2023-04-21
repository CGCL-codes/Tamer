    private void captureScreen(int monitor) {
        setScreen(monitor, systems[monitor].createScreenCapture(screenRects[monitor]));
        currentImageNos[monitor] = (currentImageNos[monitor] + 1) % Integer.MAX_VALUE;
        System.out.println("current " + currentImageNos[monitor]);
    }
