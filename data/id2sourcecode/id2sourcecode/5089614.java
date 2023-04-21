    public void setChannelControls(int chan, SynthChannelControls c) {
        CompoundControl old = getChannelControls(chan);
        if (old != null) {
            remove(old);
            old.close();
        }
        if (c != null) {
            String name = c.getName();
            if (find(name) != null) {
                disambiguate(c);
                c.setAnnotation(name);
            }
        }
        super.setChannelControls(chan, c);
        setChanged();
        notifyObservers(chan);
    }
