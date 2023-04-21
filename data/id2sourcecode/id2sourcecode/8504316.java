    protected Properties loadFile(String fileName) {
        Properties prop = new Properties();
        try {
            Bundle b = TestPlugin.getDefault().getBundle();
            String packageName = getClass().getName();
            packageName = packageName.substring(0, packageName.lastIndexOf("."));
            packageName = packageName.replace('.', File.separatorChar);
            packageName += File.separator;
            packageName += fileName;
            URL url0 = b.getResource(packageName);
            final InputStream input = url0.openStream();
            prop.load(input);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }
