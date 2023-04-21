    public short getChannelValue(short address) {
        short value;
        if (faderValues[address] >= 0) {
            value = faderValues[address];
        } else {
            if (submasterValues[address] > 0) {
                if (cueValues[address] > 0) {
                    if (submasterValues[address] >= cueValues[address]) value = submasterValues[address]; else value = cueValues[address];
                } else {
                    value = submasterValues[address];
                }
            } else {
                if (cueValues[address] > 0) value = cueValues[address]; else {
                    if (faderValues[address] == -100) value = -100; else {
                        if (cueValues[address] == -100) value = -100; else value = 0;
                    }
                }
            }
        }
        return value;
    }
