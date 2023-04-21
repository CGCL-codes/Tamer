    private void getChannelHtmlView(List<MessageAlertChannelActiveAwareComponent> channelAwareComponents, StringBuffer sbuff) {
        sbuff.append("<b>MessageAlertChannelActiveAwareComponent</b>: count=").append(channelAwareComponents.size()).append("<br><br>");
        for (MessageAlertChannelActiveAwareComponent channelComponent : channelAwareComponents) {
            genChannelComponentssRef(channelAwareComponents, sbuff);
            sbuff.append("<b><font color='FF0000'>").append(genCompentArch(channelComponent.getName())).append("</font></b><br>");
            ChannelAwareComponentView channelAwareComponentView = new ChannelAwareComponentView(channelComponent);
            viewComponent(channelAwareComponentView, sbuff);
            viewChannelssAsTable(channelComponent.getChannels(), sbuff);
        }
    }
