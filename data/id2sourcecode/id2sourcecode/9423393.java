    public Conf getChannel(String channel, String lang) throws Exception {
        Conf retVal;
        String mKey = channel;
        if (lang != null && !K.EMPTY.equals(lang)) mKey = mKey.concat(K.UNDERSCORE).concat(lang);
        retVal = msgFiles.get(mKey);
        if (retVal == null) {
            synchronized (monitor) {
                retVal = msgFiles.get(mKey.toString());
                if (retVal == null) {
                    String confFileName = FileTool.composeDirFile(ConfTool.getGlobalParameter(Mustang.CONF_DIR), mKey);
                    File f = new File(confFileName.concat(K.XML_EXTENSION));
                    if (f.exists()) retVal = ConfTool.addConf(mKey, confFileName.concat(K.XML_EXTENSION)); else retVal = ConfTool.addConf(mKey, confFileName.concat(K.PROPERTIES_EXTENSION));
                    msgFiles.put(mKey, retVal);
                    if (log.isInfo()) log.info("loaded: " + mKey);
                } else {
                    if (log.isInfo()) log.info("a concurrent request loaded: " + mKey);
                }
            }
        } else {
            if (log.isInfo()) log.info("got " + mKey + " from cache");
        }
        return retVal;
    }
