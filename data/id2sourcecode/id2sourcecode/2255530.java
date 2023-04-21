    private void initPredictorsProperties() {
        Properties prop = new Properties();
        try {
            URL url = ClassLoader.getSystemResource("predictors.properties");
            prop.load(url.openStream());
        } catch (IOException e) {
            System.err.println("Error loading properties file. error: " + e.getMessage());
        }
        for (Predictors currPred : Predictors.values()) {
            Map<String, String> currPredictorsMap = new HashMap<String, String>();
            for (PropertyKeys currPKey : PropertyKeys.values()) {
                String key = currPred.toString() + "." + currPKey.toString();
                String value = prop.getProperty(key);
                if (value != null) {
                    StringTokenizer st = new StringTokenizer(key, ".");
                    st.nextToken();
                    key = st.nextToken();
                    currPredictorsMap.put(key, value);
                }
            }
            predictorsProperties.put(currPred, currPredictorsMap);
        }
    }
