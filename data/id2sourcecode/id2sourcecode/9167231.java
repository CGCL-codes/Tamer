    public void calibrateSteering() {
        steeringMotor.setSpeed(100);
        steeringMotor.setStallThreshold(10, 100);
        steeringMotor.forward();
        while (!steeringMotor.isStalled()) Thread.yield();
        int r = steeringMotor.getTachoCount();
        steeringMotor.backward();
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (!steeringMotor.isStalled()) Thread.yield();
        int l = steeringMotor.getTachoCount();
        int center = (l + r) / 2;
        r -= center;
        l -= center;
        minRight = r;
        minLeft = l;
        steeringMotor.rotateTo(center);
        steeringMotor.resetTachoCount();
        steeringMotor.setStallThreshold(50, 1000);
    }
