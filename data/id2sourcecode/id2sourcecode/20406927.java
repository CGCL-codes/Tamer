    void startAnimation() {
        int first = firstFrame, last = lastFrame;
        if (first < 1 || first > nSlices || last < 1 || last > nSlices) {
            first = 1;
            last = nSlices;
        }
        if (swin.getAnimate()) {
            stopAnimation();
            return;
        }
        imp.unlock();
        swin.setAnimate(true);
        long time, nextTime = System.currentTimeMillis();
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
        int sliceIncrement = 1;
        Calibration cal = imp.getCalibration();
        if (cal.fps != 0.0) animationRate = cal.fps;
        if (animationRate < 0.1) animationRate = 1.0;
        int frames = imp.getNFrames();
        int slices = imp.getNSlices();
        if (imp.isDisplayedHyperStack() && frames > 1) {
            int frame = imp.getFrame();
            first = 1;
            last = frames;
            while (swin.getAnimate()) {
                time = System.currentTimeMillis();
                if (time < nextTime) IJ.wait((int) (nextTime - time)); else Thread.yield();
                nextTime += (long) (1000.0 / animationRate);
                frame += sliceIncrement;
                if (frame < first) {
                    frame = first + 1;
                    sliceIncrement = 1;
                }
                if (frame > last) {
                    if (cal.loop) {
                        frame = last - 1;
                        sliceIncrement = -1;
                    } else {
                        frame = first;
                        sliceIncrement = 1;
                    }
                }
                imp.setPosition(imp.getChannel(), imp.getSlice(), frame);
            }
            return;
        }
        if (imp.isDisplayedHyperStack() && slices > 1) {
            slice = imp.getSlice();
            first = 1;
            last = slices;
            while (swin.getAnimate()) {
                time = System.currentTimeMillis();
                if (time < nextTime) IJ.wait((int) (nextTime - time)); else Thread.yield();
                nextTime += (long) (1000.0 / animationRate);
                slice += sliceIncrement;
                if (slice < first) {
                    slice = first + 1;
                    sliceIncrement = 1;
                }
                if (slice > last) {
                    if (cal.loop) {
                        slice = last - 1;
                        sliceIncrement = -1;
                    } else {
                        slice = first;
                        sliceIncrement = 1;
                    }
                }
                imp.setPosition(imp.getChannel(), slice, imp.getFrame());
            }
            return;
        }
        long startTime = System.currentTimeMillis();
        int count = 0;
        double fps = 0.0;
        while (swin.getAnimate()) {
            time = System.currentTimeMillis();
            count++;
            if (time > startTime + 1000L) {
                startTime = System.currentTimeMillis();
                fps = count;
                count = 0;
            }
            IJ.showStatus((int) (fps + 0.5) + " fps");
            if (time < nextTime) IJ.wait((int) (nextTime - time)); else Thread.yield();
            nextTime += (long) (1000.0 / animationRate);
            slice += sliceIncrement;
            if (slice < first) {
                slice = first + 1;
                sliceIncrement = 1;
            }
            if (slice > last) {
                if (cal.loop) {
                    slice = last - 1;
                    sliceIncrement = -1;
                } else {
                    slice = first;
                    sliceIncrement = 1;
                }
            }
            swin.showSlice(slice);
        }
    }
