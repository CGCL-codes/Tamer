    private void addWaveform(WaveformArea area) throws SAXException {
        int lead = area.getWaveformIndex();
        WaveFormChannel channel = waveForms.getChannel(lead);
        WaveformScalingInfo scalingInfo = prepareScalingInfo(channel, area);
        float topPos = area.getTopY();
        float baseLineY = topPos + area.getHeight() * scalingInfo.getZeroLine();
        if (log.isDebugEnabled()) log.debug("topPos:" + topPos + ", baseLineY:" + baseLineY + ", scalingInfo:" + scalingInfo);
        channel.reset();
        addG("lead" + lead, "translate(" + (graphicXOffset + area.getLeftX()) + "," + baseLineY + ")", null, null, null);
        addPath("lead" + lead, "fill:none;stroke:" + WF_COLORS[lead % WF_COLORS.length], "5", getWaveFormString(channel, area, scalingInfo));
        util.endElement("g");
        addG("lead" + lead, "translate(" + (graphicXOffset + area.getLeftX()) + "," + (topPos + area.getHeight() * 0.5) + ")", null, null, null);
        addText("0", "-100", "30", "green", null, channel.getChSource());
        addPath("waveseparator", "fill:none;stroke:green", "5", "M 0 -90 L 0 -20 M 0 90 L 0 20 ");
        if (scalingInfo.getYScaleDesc() != null) {
            addText("0", "-150", "30", "green", null, scalingInfo.getYScaleDesc());
        }
        if (scalingInfo.getXScaleDesc() != null) {
            addText(Float.toString(area.getWidth() - 300), Float.toString(topPos + area.getHeight() - 150), "30", "green", null, scalingInfo.getXScaleDesc());
        }
        util.endElement("g");
    }
