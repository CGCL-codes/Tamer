    public static int getLinkWithClassVisualID(EObject domainElement) {
        if (domainElement == null) {
            return -1;
        }
        if (NetworkPackage.eINSTANCE.getChannel().isSuperTypeOf(domainElement.eClass())) {
            return ChannelEditPart.VISUAL_ID;
        }
        return -1;
    }
