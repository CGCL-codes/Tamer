    private Datasets loadDataSets(String strConfigXmlPath) throws Exception {
        InputStreamReader isr;
        try {
            isr = new FileReader(strConfigXmlPath);
        } catch (FileNotFoundException e) {
            URL url = this.getClass().getClassLoader().getResource(strConfigXmlPath);
            if (null != url) {
                isr = new InputStreamReader(url.openStream());
            } else {
                throw new Exception("Could not load data export configuration.");
            }
        }
        Datasets datasets = Datasets.unmarshal(isr);
        isr.close();
        return datasets;
    }
