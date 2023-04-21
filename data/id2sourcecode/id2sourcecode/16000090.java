    void _storeFC(ArkDualCasFileInterface doc, File file) throws ArUnvalidIndexException, ArFileException, ArFileWormException, FileNotFoundException {
        doc.store(ArConstants.BUFFERSIZEDEFAULT, metadata);
        FileInputStream inputStream = new FileInputStream(file);
        FileChannel fileChannelIn = inputStream.getChannel();
        dKey = doc.write(fileChannelIn);
        if (DEBUG) System.out.println("getIndex():" + doc.getIndex() + ":(0) " + doc.getGlobalPath() + ":(1) " + store1a.getObjectGlobalPath() + ":(2) " + doc.exists() + ":(3) " + dKey);
        doc.clear();
    }
