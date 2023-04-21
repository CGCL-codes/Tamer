    private void addProviders(URL url) {
        InputStream inputStream = null;
        BufferedReader reader = null;
        try {
            inputStream = url.openStream();
            reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    providers.add((BeanAdapterProvider) Class.forName(line).newInstance());
                } catch (IllegalAccessException ex) {
                } catch (InstantiationException ex) {
                } catch (ClassNotFoundException ex) {
                }
            }
        } catch (UnsupportedEncodingException ex) {
        } catch (IOException ex) {
        }
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException ex) {
            }
        }
    }
