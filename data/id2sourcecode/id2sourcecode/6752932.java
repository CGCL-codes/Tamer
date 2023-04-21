    public void initializePackageContents() {
        if (isInitialized) return;
        isInitialized = true;
        setName(eNAME);
        setNsPrefix(eNS_PREFIX);
        setNsURI(eNS_URI);
        NetworkPackage theNetworkPackage = (NetworkPackage) EPackage.Registry.INSTANCE.getEPackage(NetworkPackage.eNS_URI);
        idealChannelEClass.getESuperTypes().add(theNetworkPackage.getChannelBehavior());
        initEClass(idealChannelEClass, IdealChannel.class, "IdealChannel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        createResource(eNS_URI);
    }
