    public final void plotData(Graphics2D g2) {
        com.javable.dataview.DataStorage storage = view.getStorage();
        com.javable.dataview.DataGroup group = null;
        com.javable.dataview.DataChannel xchannel = null;
        com.javable.dataview.DataChannel ychannel = null;
        java.awt.geom.Rectangle2D.Double clip = getClip(g2);
        double xmin = clip.getX();
        double xmax = clip.getX() + clip.getWidth();
        try {
            for (int i = 0; i < storage.getGroupsSize(); i++) {
                group = storage.getGroup(i);
                xchannel = storage.getChannel(i, group.getXChannel());
                for (int j = 0; j < storage.getChannelsSize(i); j++) {
                    if (j != group.getXChannel()) {
                        ychannel = storage.getChannel(i, j);
                        plotChannel(g2, xchannel, ychannel, xmin, xmax);
                    }
                }
            }
        } catch (Exception e) {
        }
    }
