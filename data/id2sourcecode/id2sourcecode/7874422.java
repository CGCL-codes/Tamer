    private URLConnection ConnServer(String arg) {
        URLConnection uc = null;
        try {
            url = new URL(arg);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            uc = url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uc;
    }
