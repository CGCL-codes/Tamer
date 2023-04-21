    @Override
    protected Properties loadFile(String fileName) {
        Properties prop = new Properties();
        try {
            URL url0 = new File(fileName).toURI().toURL();
            final InputStream input = url0.openStream();
            prop.load(input);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }
