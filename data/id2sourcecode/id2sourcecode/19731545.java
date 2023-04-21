    public static XMultiServiceFactory create(String writeRegistryFile, String readRegistryFile, boolean readOnly) throws com.sun.star.uno.Exception {
        String vm_info = System.getProperty("java.vm.info");
        if (vm_info != null && vm_info.indexOf("green") != -1) throw new RuntimeException(RegistryServiceFactory.class.toString() + ".create - can't use binary UNO with green threads");
        if (writeRegistryFile == null && readRegistryFile == null) throw new com.sun.star.uno.Exception("No registry is specified!");
        Object obj = createRegistryServiceFactory(writeRegistryFile, readRegistryFile, readOnly, RegistryServiceFactory.class.getClassLoader());
        return (XMultiServiceFactory) UnoRuntime.queryInterface(XMultiServiceFactory.class, obj);
    }
