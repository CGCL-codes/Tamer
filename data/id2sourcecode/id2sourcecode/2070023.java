    public static InputSource openFile(String name) throws FileNotFoundException {
        try {
            if (name.startsWith("http")) {
                URL url = new URL(name);
                return new InputSource(new BufferedReader(new InputStreamReader(url.openStream())));
            } else {
                return new InputSource(new BufferedReader(new FileReader(name)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
