    private static Collection<NetworkLinkDescriptor> getIncomingTypeModelFacetLinks_Channel_4003(Node target, Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences) {
        LinkedList<NetworkLinkDescriptor> result = new LinkedList<NetworkLinkDescriptor>();
        Collection<EStructuralFeature.Setting> settings = crossReferences.get(target);
        for (EStructuralFeature.Setting setting : settings) {
            if (setting.getEStructuralFeature() != NetworkPackage.eINSTANCE.getChannel_Target() || false == setting.getEObject() instanceof Channel) {
                continue;
            }
            Channel link = (Channel) setting.getEObject();
            if (ChannelEditPart.VISUAL_ID != NetworkVisualIDRegistry.getLinkWithClassVisualID(link)) {
                continue;
            }
            Node src = link.getSource();
            result.add(new NetworkLinkDescriptor(src, target, link, NetworkElementTypes.Channel_4003, ChannelEditPart.VISUAL_ID));
        }
        return result;
    }
