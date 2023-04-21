    public static ElmStreamTokenizer getEST(String file) throws IOException {
        ElmStreamTokenizer est = null;
        try {
            if (file.startsWith("x-res:")) {
                file = file.substring(6);
                URL url = ElmVE.classLoader.getResource(file);
                InputStreamReader isr = new InputStreamReader(url.openStream());
                est = new ElmStreamTokenizer(isr);
            } else {
                URL url = new URL(file);
                InputStreamReader isr = new InputStreamReader(url.openStream());
                est = new ElmStreamTokenizer(isr);
            }
        } catch (MalformedURLException me) {
            try {
                FileReader reader = new FileReader(file);
                est = new ElmStreamTokenizer(reader);
            } catch (FileNotFoundException fe) {
                throw new IOException();
            }
        }
        return est;
    }
