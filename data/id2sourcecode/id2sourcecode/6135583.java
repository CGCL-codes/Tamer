    public void graphChanged(GraphModelEvent e) {
        for (Object o : e.getChange().getChanged()) if (o instanceof MagicGraphCell) {
            MagicGraphCell cell = (MagicGraphCell) o;
            double x = GraphConstants.getBounds(cell.getAttributes()).getCenterX();
            double y = GraphConstants.getBounds(cell.getAttributes()).getCenterY();
            _actInt.interprete(new ACTChanLocation(cell.getChannelNumber(), x, y));
        }
    }
