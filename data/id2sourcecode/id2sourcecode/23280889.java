    private static final RGBBase.Channel getTargetChannel(LCDTarget lcdTarget) {
        LCDTarget.Number number = lcdTarget.getNumber();
        switch(number) {
            case Ramp256W:
                return RGBBase.Channel.W;
            case Ramp256R:
            case Ramp256R_W:
                return RGBBase.Channel.R;
            case Ramp256G:
            case Ramp256G_W:
                return RGBBase.Channel.G;
            case Ramp256B:
            case Ramp256B_W:
                return RGBBase.Channel.B;
            default:
                return null;
        }
    }
