    public void init() {
        IClientModelParser parser = ClientModelParserFactory.INSTANCE.createParser();
        InputStream[] resources = new InputStream[this.configPaths.length];
        URL url = null;
        int i = 0;
        for (String configPath : configPaths) {
            try {
                url = new URL(configPath);
                resources[i] = url.openStream();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            i++;
        }
        parser.setResources(resources);
        parser.parser();
        this.serviceClient = new ServiceClientImpl();
    }
