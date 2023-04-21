    public void update() {
        setChannelMap(getChannelMap());
        if (changeMask != 0) {
            changeMask = 0;
        }
        super.update();
    }
