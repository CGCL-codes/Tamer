    public void centerAndZoomOnChannel(String channelId) {
        Polyline line = getChannelManager().getPolyline(channelId);
        if (line == null) {
            return;
        }
        LatLngBounds bounds = line.getBounds();
        getMap().panTo(bounds.getCenter());
        while (!getMap().getBounds().containsBounds(bounds)) {
            getMap().setZoomLevel(getMap().getZoomLevel() - 1);
        }
        new ChannelClickHandler(this).onClick(new PolylineClickEvent(line, line.getVertex(0)));
    }
