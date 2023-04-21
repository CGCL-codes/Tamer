    public static void createExtends() {
        if (MODEL == null && PACKAGE == null && METAMODEL == null) {
            return;
        }
        if (repository.getExtent(MODEL) != null) {
            return;
        }
        ModelPackage fooMMExtent = (ModelPackage) repository.getExtent(METAMODEL);
        try {
            if (fooMMExtent == null) {
                fooMMExtent = (ModelPackage) repository.createExtent(METAMODEL);
            }
            MofPackage fooMMPackage = findMDRmanagerPackage(fooMMExtent);
            if (fooMMPackage == null) {
                if (Metamodelfile != null) {
                    File f = new File(Metamodelfile);
                    loadModel(f, fooMMExtent);
                } else if (MetamodelURL != null) {
                    java.net.URL url = new java.net.URL(MetamodelURL);
                    loadModel(url.openStream(), fooMMExtent);
                } else {
                    loadModel(MetamodelData, fooMMExtent);
                }
                fooMMPackage = findMDRmanagerPackage(fooMMExtent);
            }
            repository.createExtent(MODEL, fooMMPackage);
        } catch (Exception saex) {
            saex.printStackTrace();
        }
    }
