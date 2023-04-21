    public static IMEExecutor BuildMEExecutor(String info, gatech.mmpm.IDomain idomain) throws ConfigurationException {
        IMEExecutor meExecutor = null;
        String[] splitInfo = info.split("@@@");
        if ((splitInfo.length < 3) || (splitInfo.length > 4)) throw new ConfigurationException("Unexpected Format. It should be: MEExecutorClassName@@@MEStoredway@@@MEDirection[@@@DecoratorClassName]");
        meExecutor = BuildMEExecutorInternal(splitInfo[0], "gatech.mmpm.learningengine.IMEExecutor");
        if (splitInfo.length > 3) {
            LazyMEExecutor lazyMEExecutor = (LazyMEExecutor) BuildMEExecutorInternal(splitInfo[3], "gatech.mmpm.learningengine.LazyMEExecutor");
            lazyMEExecutor.setMEOrig(meExecutor);
            meExecutor = lazyMEExecutor;
        }
        java.io.InputStream me = null;
        if (splitInfo[1].equals("file")) try {
            me = new java.io.FileInputStream(splitInfo[2]);
        } catch (Exception e) {
            try {
                URL url = new URL(splitInfo[2]);
                me = url.openStream();
            } catch (Exception ex) {
                throw new ConfigurationException(splitInfo[2] + ": File not found.");
            }
        } else if (splitInfo[1].equals("zipfile")) try {
            me = getMEFromZipFile(splitInfo[2]);
        } catch (Exception e) {
            throw new ConfigurationException(splitInfo[2] + ": File not found.");
        } else throw new ConfigurationException(splitInfo[1] + " is not a valid way of ME retrieval.");
        meExecutor.loadME(me, idomain);
        return meExecutor;
    }
