    public static List<NetworkLinkDescriptor> getOutgoingLinks(View view) {
        switch(NetworkVisualIDRegistry.getVisualID(view)) {
            case NodeEditPart.VISUAL_ID:
                return getNode_2001OutgoingLinks(view);
            case ChannelEditPart.VISUAL_ID:
                return getChannel_4003OutgoingLinks(view);
        }
        return Collections.emptyList();
    }
