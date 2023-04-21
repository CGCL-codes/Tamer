    private void performPitchShift(AChannelSelection ch1, AChannelSelection ch2) {
        MMArray s1 = ch1.getChannel().getSamples();
        int o1 = ch1.getOffset();
        int l1 = ch1.getLength();
        ch1.getChannel().markChange();
        try {
            MMArray tmp = new MMArray(l1, 0);
            tmp.copy(s1, o1, 0, tmp.getLength());
            int stepWidth = buffer.getLength() - transitionLength;
            int stepBegin = o1;
            while (stepBegin < o1 + l1) {
                for (int i = 0; i < buffer.getLength(); i++) {
                    float index;
                    if (ch2 != null) {
                        index = stepBegin - o1 + (float) i * ch2.getChannel().getSamples().get(stepBegin);
                    } else {
                        index = stepBegin - o1 + (float) i * shiftFactor;
                    }
                    if (index < l1) {
                        buffer.set(i, AOToolkit.interpolate3(tmp, index));
                    } else {
                        buffer.set(i, AOToolkit.interpolate3(tmp, i));
                    }
                }
                for (int i = 0; i < buffer.getLength(); i++) {
                    if (stepBegin + i < o1 + l1) {
                        float s;
                        if (i < transitionLength) {
                            float f3 = ((float) i) / transitionLength;
                            f3 *= f3;
                            float f4 = ((float) (transitionLength - i)) / transitionLength;
                            f4 *= f4;
                            s = s1.get(stepBegin + i) * f4 + buffer.get(i) * f3;
                        } else {
                            s = buffer.get(i);
                        }
                        s1.set(stepBegin + i, ch1.mixIntensity(stepBegin + i, s1.get(stepBegin + i), s));
                    }
                }
                stepBegin += stepWidth;
            }
            AOToolkit.applyZeroCross(s1, o1);
            AOToolkit.applyZeroCross(s1, o1 + l1);
        } catch (ArrayIndexOutOfBoundsException oob) {
            Debug.printStackTrace(5, oob);
        }
    }
