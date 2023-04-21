    protected void calcVolume() {
        synchronized (control_mutex) {
            double gain = Math.pow(10.0, gain_control.getValue() / 20.0);
            if (mute_control.getValue()) gain = 0;
            leftgain = (float) gain;
            rightgain = (float) gain;
            if (mixer.getFormat().getChannels() > 1) {
                double balance = balance_control.getValue();
                if (balance > 0) leftgain *= (1 - balance); else rightgain *= (1 + balance);
            }
        }
        eff1gain = (float) Math.pow(10.0, reverbsend_control.getValue() / 20.0);
        eff2gain = (float) Math.pow(10.0, chorussend_control.getValue() / 20.0);
        if (!apply_reverb.getValue()) {
            eff1gain = 0;
        }
    }
