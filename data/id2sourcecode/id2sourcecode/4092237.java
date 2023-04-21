    protected Image createImage(String name) {
        try {
            URL url = getClass().getResource(name);
            return stream2Image(url.openStream());
        } catch (Exception e) {
        }
        try {
            File file = new File(name);
            if (file.exists()) {
                return stream2Image(new FileInputStream(file));
            }
        } catch (IOException e) {
        }
        return null;
    }
