    public void operate(AChannelSelection ch1) {
        MMArray s1 = ch1.getChannel().getSamples();
        int xStart;
        int xEnd;
        MMArray tmp;
        if (selectionIndependent) {
            xStart = (int) x.get(0);
            xEnd = (int) x.get(x.getLength() - 1);
        } else {
            xStart = ch1.getOffset();
            xEnd = ch1.getLength() + ch1.getOffset();
        }
        tmp = new MMArray(xEnd - xStart, 0);
        ch1.getChannel().markChange();
        switch(order) {
            case SINGLE_POINTS:
                for (int i = 0; i < x.getLength(); i++) {
                    if (ch1.getChannel().getSamples().isInRange((int) x.get(i))) {
                        if (selectionIndependent) {
                            s1.set((int) x.get(i), y.get(i));
                        } else {
                            int ii = (int) x.get(i);
                            if (ch1.isSelected(ii)) {
                                s1.set(ii, ch1.mixIntensity(ii, s1.get(ii), y.get(i)));
                            }
                        }
                    }
                }
                break;
            case ORDER_0:
                for (int i = xStart; i < xEnd; i++) {
                    if (ch1.getChannel().getSamples().isInRange(i)) {
                        tmp.set(i - xStart, AOToolkit.interpolate0(x, y, (float) i));
                    }
                }
                break;
            case ORDER_1:
                for (int i = xStart; i < xEnd; i++) {
                    if (ch1.getChannel().getSamples().isInRange(i)) {
                        tmp.set(i - xStart, AOToolkit.interpolate1(x, y, (float) i));
                    }
                }
                break;
            case ORDER_2:
                for (int i = xStart; i < xEnd; i++) {
                    if (ch1.getChannel().getSamples().isInRange(i)) {
                        tmp.set(i - xStart, AOToolkit.interpolate2(x, y, (float) i));
                    }
                }
                break;
            case ORDER_3:
                for (int i = xStart; i < xEnd; i++) {
                    if (ch1.getChannel().getSamples().isInRange(i)) {
                        tmp.set(i - xStart, AOToolkit.interpolate3(x, y, (float) i));
                    }
                }
                break;
            case SPLINE:
                AOSpline spline = AOToolkit.createSpline();
                spline.load(x, y);
                for (int i = xStart; i < xEnd; i++) {
                    if (ch1.getChannel().getSamples().isInRange(i)) {
                        tmp.set(i - xStart, spline.getResult((float) i));
                    }
                }
                break;
        }
        switch(order) {
            case ORDER_0:
            case ORDER_1:
            case ORDER_2:
            case ORDER_3:
            case SPLINE:
                switch(operation) {
                    case REPLACE_OPERATION:
                        for (int i = 0; i < tmp.getLength(); i++) {
                            if (ch1.getChannel().getSamples().isInRange(i + xStart)) {
                                if (selectionIndependent) {
                                    s1.set(i + xStart, tmp.get(i));
                                } else {
                                    int ii = i + xStart;
                                    s1.set(ii, ch1.mixIntensity(ii, s1.get(ii), tmp.get(i)));
                                }
                            }
                        }
                        break;
                    case ENVELOPE_OPERATION:
                        float factor = AOToolkit.max(s1, xStart, xEnd - xStart);
                        for (int i = 0; i < tmp.getLength(); i++) {
                            if (ch1.getChannel().getSamples().isInRange(i + xStart)) {
                                if (selectionIndependent) {
                                    s1.set(i + xStart, s1.get(i + xStart) * tmp.get(i) / factor);
                                } else {
                                    int ii = i + xStart;
                                    float x = s1.get(ii) * tmp.get(i) / factor;
                                    s1.set(ii, ch1.mixIntensity(ii, s1.get(ii), x));
                                }
                            }
                        }
                        break;
                }
                break;
        }
    }
