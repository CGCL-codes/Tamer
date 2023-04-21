    public void hideChannelLines(boolean hide) {
        if (getChannelManager() != null) {
            for (Polyline line : getChannelManager().getPolylines()) {
                line.setVisible(!hide);
            }
        }
    }
