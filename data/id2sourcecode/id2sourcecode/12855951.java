    public static List<NetworkLinkDescriptor> getIncomingLinks(View view) {
        switch(NetworkVisualIDRegistry.getVisualID(view)) {
            case NodeEditPart.VISUAL_ID:
                return getNode_2001IncomingLinks(view);
            case ChannelEditPart.VISUAL_ID:
                return getChannel_4003IncomingLinks(view);
        }
        return Collections.emptyList();
    }
