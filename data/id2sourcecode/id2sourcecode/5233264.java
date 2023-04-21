    public void actuate(double leftPower, double rightPower) {
        lOutPow = (lOutPow + leftPower) / 2;
        rOutPow = (rOutPow + rightPower) / 2;
        double distWalked = (lOutPow + rOutPow) / 2;
        rotation += (rOutPow - lOutPow);
        while (rotation > Math.PI) rotation -= Math.PI * 2;
        while (rotation < -Math.PI) rotation += Math.PI * 2;
        x += Math.cos(rotation) * distWalked;
        y += Math.sin(rotation) * distWalked;
    }
