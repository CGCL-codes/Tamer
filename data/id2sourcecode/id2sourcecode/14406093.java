    public static void init() {
        try {
            URL url = Configuration.class.getResource("/constants.properties");
            properties.load(url.openConnection().getInputStream());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
