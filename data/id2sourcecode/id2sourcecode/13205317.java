    public SeasrAnalytics loadConfig() throws Exception {
        Unmarshaller unmarshaller = getUnmarshaller();
        unmarshaller.setValidation(false);
        SEASR_CONFIG = (String) edu.tufts.vue.preferences.implementations.SeasrRepositoryPreference.getInstance().getValue();
        System.out.println("Seasr config: " + SEASR_CONFIG);
        InputStream inputstream = null;
        try {
            URL url = new URL(SEASR_CONFIG);
            inputstream = url.openStream();
        } catch (MalformedURLException mex) {
            System.out.println("SeasrConfigLoader.loadConfig: " + mex);
            inputstream = this.getClass().getResourceAsStream(DEFAULT_SEASR_CONFIG);
        } catch (IOException iox) {
            System.out.println("SeasrConfigLoader.loadConfig: " + iox);
            inputstream = this.getClass().getResourceAsStream(DEFAULT_SEASR_CONFIG);
        }
        SeasrAnalytics sa = (SeasrAnalytics) unmarshaller.unmarshal(new InputSource(inputstream));
        return sa;
    }
