    public void init() {
        double a, a_;
        String s;
        try {
            VariableStorage vs = VariableStorage.getInstance();
            lon = vs.getIntegerVariable(GPSProvider.VS_PREFIX, GPSProvider.VS_LAST_POSITION_X) / 100000;
            lat = vs.getIntegerVariable(GPSProvider.VS_PREFIX, GPSProvider.VS_LAST_POSITION_Y) / 100000;
        } catch (StorageSystemException sse) {
            sse.showErrorMessage();
        }
        switch(choice) {
            case 0:
                {
                    this.setTitle(Texts.SUN_MOON_POSITION);
                    a = SunEquations.getAzimuth(lon, lat, Times.getJD());
                    s = getStringDegrees(a);
                    s1.setText(s);
                    a = SunEquations.getAltitude(lon, lat, Times.getJD());
                    s = getStringDegrees(a);
                    s2.setText(s + "\n");
                    append(s1);
                    append(s2);
                    break;
                }
            case 1:
                {
                    this.setTitle(Texts.SUN_MOON_RISE + ", " + Texts.SUN_MOON_TRANSIT + ", " + Texts.SUN_MOON_SET);
                    double a1 = SunEquations.getTwilight(-0.8333, true, lat, t.getJD2());
                    if (a1 == -1) {
                        s3.setText(Texts.UNI_NONE);
                    } else {
                        s = getStringHours(a1);
                        a_ = a1 - offset;
                        t = new Times(t.l_Y, t.l_M, t.l_D, a_);
                        double a12 = SunEquations.getAzimuth(lon, lat, t.getJD());
                        s += ("\n" + Texts.UNI_AZIMUTH + ": " + getStringDegrees(a12));
                        s3.setText(s);
                    }
                    double a2 = SunEquations.getTwilight(-0.8333, false, lat, t.getJD2());
                    if (a2 == -1) {
                        s5.setText(Texts.UNI_NONE);
                    } else {
                        s = getStringHours(a2);
                        a_ = a2 - offset;
                        t = new Times(t.l_Y, t.l_M, t.l_D, a_);
                        double a22 = SunEquations.getAzimuth(lon, lat, t.getJD());
                        s += ("\n" + Texts.UNI_AZIMUTH + ": " + getStringDegrees(a22));
                        s5.setText(s + "\n");
                    }
                    a = (a1 + a2) / 2;
                    if (a == -1) {
                        s4.setText(Texts.UNI_NONE);
                    } else {
                        s = getStringHours(a);
                        t = new Times(t.l_Y, t.l_M, t.l_D, a - offset);
                        double a22 = SunEquations.getAltitude(lon, lat, t.getJD());
                        s += ("\n" + Texts.UNI_ALTITUDE + ": " + getStringDegrees(a22));
                        s4.setText(s);
                    }
                    append(s3);
                    append(s4);
                    append(s5);
                    break;
                }
            case 2:
                {
                    this.setTitle(Texts.SUN_MOON_TWILIGHTS);
                    append(Texts.SUN_MOON_TW_CIVIL + ":\n");
                    a = SunEquations.getTwilight(SunEquations.DEVIATION_CIVIL, true, lat, t.getJD2());
                    if (a == -1) {
                        s = Texts.UNI_NONE + "\n";
                    } else {
                        s = getStringHours(a);
                    }
                    a = SunEquations.getTwilight(SunEquations.DEVIATION_CIVIL, false, lat, t.getJD2());
                    if (a == -1) {
                        s = Texts.UNI_NONE + "\n";
                    } else {
                        s += " - " + getStringHours(a) + "\n";
                    }
                    append(s);
                    append(Texts.SUN_MOON_TW_NAUTICAL + ":\n");
                    a = SunEquations.getTwilight(SunEquations.DEVIATION_NAUTICAL, true, lat, t.getJD2());
                    if (a == -1) {
                        s = Texts.UNI_NONE + "\n";
                    } else {
                        s = getStringHours(a);
                    }
                    a = SunEquations.getTwilight(SunEquations.DEVIATION_NAUTICAL, false, lat, t.getJD2());
                    if (a == -1) {
                        s = Texts.UNI_NONE + "\n";
                    } else {
                        s += " - " + getStringHours(a) + "\n";
                    }
                    append(s);
                    append(Texts.SUN_MOON_TW_ASTRONOMICAL + "\n");
                    a = SunEquations.getTwilight(SunEquations.DEVIATION_ASTRONOMICAL, true, lat, t.getJD2());
                    if (a == -1) {
                        s6.setText(Texts.UNI_NONE);
                    } else {
                        s = getStringHours(a);
                    }
                    a = SunEquations.getTwilight(SunEquations.DEVIATION_ASTRONOMICAL, false, lat, t.getJD2());
                    if (a == -1) {
                        s7.setText(Texts.UNI_NONE);
                    } else {
                        s += " - " + getStringHours(a);
                    }
                    append(s);
                    break;
                }
            default:
                break;
        }
    }
