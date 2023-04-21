    public void operateChannel0WithChannel1WithChannel2(AOperation o) {
        o.startOperation();
        if (getChannelSelection(0).isSelected()) {
            o.operate(getChannelSelection(0), getChannelSelection(1), getChannelSelection(2));
        }
        o.endOperation();
        System.gc();
    }
