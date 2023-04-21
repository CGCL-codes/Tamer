    protected final LCDTarget initMeasure(RGBBase.Channel channel, boolean ramp256Measure) {
        cpm.reset();
        LCDTargetBase.Number number = MeasuredUtils.getMeasureNumber(channel, ramp256Measure, false);
        LCDTarget ramp = null;
        if (number == LCDTargetBase.Number.FiveColor) {
            RGB[] wrgbk = new RGB[5];
            for (int x = 0; x < 4; x++) {
                wrgbk[x] = (RGB) cpcodeRGBArray[cpcodeRGBArray.length - 1].clone();
            }
            for (int x = 1; x < 4; x++) {
                RGBBase.Channel ch = RGBBase.Channel.getChannel(x);
                wrgbk[x].reserveValue(ch);
            }
            wrgbk[4] = (RGB) cpcodeRGBArray[0].clone();
            ramp = this.measure(wrgbk);
        } else if (number == LCDTargetBase.Number.BlackAndWhite) {
            RGB[] bw = new RGB[2];
            bw[0] = (RGB) cpcodeRGBArray[0].clone();
            bw[1] = (RGB) cpcodeRGBArray[cpcodeRGBArray.length - 1].clone();
            ramp = mp.whiteSequenceMeasure ? this.measureWhite(bw) : this.measure(bw);
        } else {
            ramp = this.measure(this.cpcodeRGBArray, channel, null, false, true);
            number = ramp.getNumber();
        }
        ramp = LCDTargetUtils.getLCDTargetWithLinearRGB(ramp, number);
        this.initRelativeTarget(ramp, channel);
        return ramp;
    }
