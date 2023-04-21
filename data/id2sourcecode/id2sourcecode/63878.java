    private static CabinetFactory getCabinetFactoryByProps() {
        try {
            CabinetFactory cabinetFactory = null;
            Factory fileFactory = (Factory) FactoryManager.getDefaultFactory("org.okip.service.filing.api", null);
            java.util.Map desiredProperties = new java.util.Hashtable();
            desiredProperties.put(new CapabilityType("MIT", "readwrite"), new Boolean(true));
            CabinetFactory cabinetFactories[] = fileFactory.getCabinetFactory(desiredProperties);
            outln("got " + cabinetFactories.length + " cabinetFactory matching properties " + desiredProperties);
            if (cabinetFactories.length <= 0) {
                errout("unable to obtain CabinetFactory with properties [" + desiredProperties + "]");
                return null;
            }
            for (int i = 0; i < cabinetFactories.length; i++) {
                outln("CF#" + i + ": " + cabinetFactories[i]);
                outln("CF#" + i + ": props=" + cabinetFactories[i].getProperties());
            }
            cabinetFactory = cabinetFactories[0];
            outln("returning cabinetFactory " + cabinetFactory + " PROPS=" + cabinetFactory.getProperties());
            return cabinetFactory;
        } catch (Exception e) {
            errout(e);
            return null;
        }
    }
