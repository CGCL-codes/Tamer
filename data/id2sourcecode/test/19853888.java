    public String getText(View view) {
        if (view.getElement() != null && view.getElement().eIsProxy()) {
            return getUnresolvedDomainElementProxyText(view);
        }
        switch(NetworkVisualIDRegistry.getVisualID(view)) {
            case ChannelEditPart.VISUAL_ID:
                return getChannel_4003Text(view);
            case NetworkEditPart.VISUAL_ID:
                return getNetwork_1000Text(view);
            case NodeEditPart.VISUAL_ID:
                return getNode_2001Text(view);
        }
        return getUnknownElementText(view);
    }
