    private void morphVolume(Part[] tmorph, double pos, int vol1, int vol2) {
        if (VB_VOL) PO.p("\nmorphing volume");
        if (tmorph[0].getChannel() == tmorph[1].getChannel()) {
            if (VB_VOL) PO.p("same channel");
            pos = RMath.linearFunc(pos, (1 / this.volEarl.getValue()), this.volCO.getValue() - 0.5);
            tmorph[0].setVolume((int) (vol1 + (vol2 - vol1) * pos));
            tmorph[1].setVolume(tmorph[0].getVolume());
        } else {
            if (VB_VOL) PO.p("not same channel. pre \n vol 1 " + vol1 + " vol 2 " + vol2);
            vol1 = (int) (vol1 * 1.0 * RMath.boundHard((1.0 - pos) * (1.0 / this.volEarl.getValue()) + this.volCO.getValue() - 0.5));
            if (VB_VOL) PO.p("new vol2 = " + vol2 + " * " + (1.0 * RMath.boundHard((pos))) + " * " + (1.0 / this.volEarl.getValue()) + " + " + (this.volCO2.getValue() - 0.5) + " = ");
            vol2 = (int) (vol2 * 1.0 * RMath.boundHard((pos) * (1.0 / this.volEarl.getValue()) + this.volCO2.getValue() - 0.5));
            if (VB_VOL) PO.p(vol2 + " ");
            tmorph[0].setVolume(vol1);
            tmorph[1].setVolume(vol2);
            if (VB_VOL) PO.p("post morphing volume \n vol 1 " + vol1 + " vol 2 " + vol2);
        }
    }
