    public short getChannelValue(short address) {
        IValueSet[] sets = channelSets.getHighPrioritySets(address);
        int averageValue = 0;
        int addValue = 0;
        int len = 0;
        boolean hasValue = false;
        for (IValueSet s : sets) {
            if (s.getChannelValue(address) >= 0) {
                hasValue = true;
                if (s.getCombineMethod() == IValueSet.ADD) addValue += s.getChannelValue(address); else if (s.getCombineMethod() == IValueSet.AVERAGE) {
                    averageValue += s.getChannelValue(address);
                    len++;
                }
            }
        }
        if (hasValue == false) return -100;
        if (addValue != 0) return (short) ((1.0 * (addValue + averageValue)) / (len + 1) + 0.5);
        if (len != 0) return (short) ((1.0 * averageValue) / len);
        return 0;
    }
