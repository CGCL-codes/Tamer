    public void operateChannel0WithChannel1(AOperation o) {
        o.startOperation();
        if (getChannelSelection(0).isSelected()) {
            o.operate(getChannelSelection(0), getChannelSelection(1));
        }
        o.endOperation();
        System.gc();
    }
