    private Document getConfigDocument() throws Exception {
        if (configDoc == null) {
            File cfgFile = new File(filePath);
            if (!cfgFile.exists()) {
                cfgFile.createNewFile();
                try {
                    InputStream fis = getClass().getResourceAsStream("/com/dgtalize/netc/business/config.xml");
                    FileOutputStream fos = new FileOutputStream(cfgFile);
                    byte[] buf = new byte[1024];
                    int readCant = 0;
                    while ((readCant = fis.read(buf)) != -1) {
                        fos.write(buf, 0, readCant);
                    }
                    fis.close();
                    fos.close();
                } catch (Exception ex) {
                    cfgFile.delete();
                    throw ex;
                }
            }
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            configDoc = db.parse(filePath);
            configDoc.getDocumentElement().normalize();
        }
        return configDoc;
    }
