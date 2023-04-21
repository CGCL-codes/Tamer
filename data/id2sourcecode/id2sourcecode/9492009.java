    public void setCrossLoc(int x, int y, int z) {
        crossLoc.setLocation(x, y);
        if (hyperstack) imp.setPosition(imp.getChannel(), z + 1, imp.getFrame()); else imp.setSlice(z + 1);
        update();
    }
