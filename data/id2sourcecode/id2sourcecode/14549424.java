    public void operate(AChannelSelection ch1) {
        MMArray s1 = ch1.getChannel().getSamples();
        int o1 = ch1.getOffset();
        int l1 = ch1.getLength();
        ch1.getChannel().markChange();
        for (int i = o1; i < (o1 + l1); i++) {
            try {
                int newSample = 0;
                if (newByte0Location < EMPTY) newSample |= ((int) s1.get(i) & 0x000000FF) << (newByte0Location * 8);
                if (newByte1Location < EMPTY) newSample |= (((int) s1.get(i) >> 8) & 0x000000FF) << (newByte1Location * 8);
                if (newByte2Location < EMPTY) newSample |= (((int) s1.get(i) >> 16) & 0x000000FF) << (newByte2Location * 8);
                if (newByte3Location < EMPTY) newSample |= (((int) s1.get(i) >> 24) & 0x000000FF) << (newByte3Location * 8);
                s1.set(i, ch1.mixIntensity(i, s1.get(i), (float) newSample));
            } catch (ArrayIndexOutOfBoundsException oob) {
            }
        }
    }
