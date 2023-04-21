    public void onClick(PolylineClickEvent event) {
        final Polyline line = event.getSender();
        currentlySelectedLine = line;
        final XSection xSection = mapPanel.getChannelManager().getXSectionFor(line);
        currentlySelectedXSection = xSection;
        if (xSection == null) {
            mapPanel.showErrorMessage("The line clicked was not a xsection ? Try again.");
            return;
        }
        if (mapPanel.isInEditMode() && mapPanel.isInDeletingMode()) {
            mapPanel.getChannelManager().removeXSection(xSection);
            return;
        }
        final Channel channel = ModelUtils.getChannelForXSection(xSection, mapPanel.getChannelManager().getChannels());
        int index = 0;
        int xSectionIndex = 0;
        for (XSection xs : channel.getXsections()) {
            Polyline xsline = mapPanel.getChannelManager().getXsectionLineFor(xs);
            if (xs == xSection) {
                if (mapPanel.isInEditMode()) {
                    xsline.setEditingEnabled(true);
                }
                xsline.setStrokeStyle(redLineStyle);
                xSectionIndex = index;
            } else {
                if (mapPanel.isInEditMode()) {
                    xsline.setEditingEnabled(false);
                }
                xsline.setStrokeStyle(greenLineStyle);
            }
            index++;
        }
        if (!mapPanel.isInEditMode()) {
            xsEditorPanel = getXsEditorPanel();
            mapPanel.getInfoPanel().clear();
            mapPanel.getInfoPanel().add(xsEditorPanel);
            xsEditorPanel.draw(channel, xSectionIndex, mapPanel);
        } else {
            xsEditorPanel = getXsEditorPanel();
            setPolylineInEditMode(line, xSection, channel);
            mapPanel.getInfoPanel().clear();
            mapPanel.getInfoPanel().add(xsEditorPanel);
            xsEditorPanel.draw(channel, xSectionIndex, mapPanel);
        }
    }
