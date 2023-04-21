    public void initializePackageContents() {
        if (isInitialized) return;
        isInitialized = true;
        setName(eNAME);
        setNsPrefix(eNS_PREFIX);
        setNsURI(eNS_URI);
        NetworkPackage theNetworkPackage = (NetworkPackage) EPackage.Registry.INSTANCE.getEPackage(NetworkPackage.eNS_URI);
        bpsKoverAWGNChannelBehaviourEClass.getESuperTypes().add(theNetworkPackage.getChannelBehavior());
        initEClass(bpsKoverAWGNChannelBehaviourEClass, BPSKoverAWGNChannelBehaviour.class, "BPSKoverAWGNChannelBehaviour", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getBPSKoverAWGNChannelBehaviour_Sigma(), ecorePackage.getEDouble(), "sigma", "1", 0, 1, BPSKoverAWGNChannelBehaviour.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        createResource(eNS_URI);
    }
