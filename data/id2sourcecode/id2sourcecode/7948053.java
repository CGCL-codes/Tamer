    public synchronized void addChannel(IOType io) {
        VisibleChannel newVisibleChannel = new VisibleChannel(visibleChannels.size(), io);
        for (Iterator<VisibleChannel> iterator = visibleChannels.iterator(); iterator.hasNext(); ) {
            VisibleChannel channel = iterator.next();
            if (channel.equals(newVisibleChannel)) {
                return;
            }
        }
        visibleChannels.add(newVisibleChannel);
        newVisibleChannel.channel = recorderModel.getChannelByName(newVisibleChannel.io.getPublicName());
        Panel panel = (Panel) getFigure();
        Color fg = ColorConstants.black;
        Polyline polyline = new Polyline();
        RGBType channelColor = newVisibleChannel.io.getRecorder().getPresentationColor();
        if (channelColor != null) {
            fg = new Color(null, channelColor.getRed(), channelColor.getGreen(), channelColor.getBlue());
            polyline.setForegroundColor(fg);
        }
        polyline.setLineWidth(newVisibleChannel.io.getPort().getBitLength() > 1 ? 2 : 1);
        polyline.setOpaque(true);
        panel.add(polyline);
        org.eclipse.draw2d.Label captionValue = new Label("");
        captionValue.setLocation(new Point(7 + CAPTION_HIGHT + 2 + CAPTION_LENGTH * (visibleChannels.size() - 1), CAPTION_VPOS + CAPTION_HIGHT));
        captionValue.setSize(CAPTION_LENGTH - (CAPTION_HIGHT + 2), CAPTION_HIGHT);
        captionValue.setLabelAlignment(PositionConstants.LEFT);
        panel.add(captionValue);
        org.eclipse.draw2d.RectangleFigure captionRect = new RectangleFigure();
        captionRect.setLocation(new Point(7 + CAPTION_LENGTH * (visibleChannels.size() - 1), CAPTION_VPOS));
        captionRect.setSize(CAPTION_HIGHT, CAPTION_HIGHT);
        captionRect.setForegroundColor(ColorConstants.black);
        captionRect.setBackgroundColor(fg);
        captionRect.setFill(true);
        panel.add(captionRect);
        org.eclipse.draw2d.Label captionLabel = new Label(io.getPublicName());
        captionLabel.setLocation(new Point(7 + CAPTION_HIGHT + 2 + CAPTION_LENGTH * (visibleChannels.size() - 1), CAPTION_VPOS));
        captionLabel.setSize(CAPTION_LENGTH - (CAPTION_HIGHT + 2), CAPTION_HIGHT);
        captionLabel.setLabelAlignment(PositionConstants.LEFT);
        panel.add(captionLabel);
    }
