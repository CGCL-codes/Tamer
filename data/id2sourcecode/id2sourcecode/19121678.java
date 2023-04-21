    public float getChannelValue(int index, boolean autocal) {
        if (autocal) {
            return channels[index].autoCal(input.getComponents()[channels[index].getComponent()].getPollData());
        } else {
            return channels[index].getValue(input.getComponents()[channels[index].getComponent()].getPollData());
        }
    }
