    public void removeXSection(XSection xSection) {
        Polyline polyline = xsectionLineMap.get(xSection);
        mapPanel.getMap().removeOverlay(polyline);
        Channel channel = channels.getChannel(xSection.getChannelId());
        channel.getXsections().remove(xSection);
        xsectionLineMap.remove(xSection);
    }
