    public RPCServo(RPCConnection out, int key, int leftPWM, double leftAngle, int rightPWM, double rightAngle) {
        this.out = out;
        this.key = key;
        pwmRange = rightPWM - leftPWM;
        average = (rightPWM + leftPWM) / 2;
        rotationRange = rightAngle - leftAngle;
    }
