    protected ChannelURLType getChannelURlType(String lifecycle) {
        if (PortletRequest.ACTION_PHASE.equals(lifecycle)) {
            return ChannelURLType.ACTION;
        } else if (PortletRequest.RENDER_PHASE.equals(lifecycle)) {
            return ChannelURLType.RENDER;
        } else if (PortletRequest.RESOURCE_PHASE.equals(lifecycle)) {
            return ChannelURLType.RESOURCE;
        } else {
            return ChannelURLType.RENDER;
        }
    }
