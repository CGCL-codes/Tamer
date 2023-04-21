    public static ENamedElement getElement(IAdaptable hint) {
        Object type = hint.getAdapter(IElementType.class);
        if (elements == null) {
            elements = new IdentityHashMap<IElementType, ENamedElement>();
            elements.put(Network_1000, NetworkPackage.eINSTANCE.getNetwork());
            elements.put(Node_2001, NetworkPackage.eINSTANCE.getNode());
            elements.put(Channel_4003, NetworkPackage.eINSTANCE.getChannel());
        }
        return (ENamedElement) elements.get(type);
    }
