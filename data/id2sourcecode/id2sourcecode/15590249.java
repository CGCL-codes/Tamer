    public void selectChannel(double x, double y) {
        com.javable.dataview.DataStorage storage = view.getStorage();
        Point2D.Double dstTop = new Point2D.Double();
        Point2D.Double dstBtm = new Point2D.Double();
        try {
            transform = view.getTransform();
            transform.inverseTransform((Point2D) (new Point2D.Double(x, y - hotspot)), dstTop);
            transform.inverseTransform((Point2D) (new Point2D.Double(x, y + hotspot)), dstBtm);
            for (int i = 0; i < storage.getGroupsSize(); i++) {
                com.javable.dataview.DataGroup group = storage.getGroup(i);
                com.javable.dataview.DataChannel xchannel = storage.getChannel(i, group.getXChannel());
                for (int j = 0; j < storage.getChannelsSize(i); j++) {
                    double tst = com.javable.dataview.analysis.ChannelStats.getValueAtX(dstTop.getX(), xchannel, storage.getChannel(i, j));
                    if (tst > dstBtm.getY() && tst < dstTop.getY()) view.fireSelectChannel(storage.getChannelNode(i, j));
                }
            }
        } catch (Exception e) {
        }
    }
