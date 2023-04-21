    public void grabHttp() {
        InputStream is = null;
        try {
            is = url.openStream();
            httpString = (new Scanner(is).useDelimiter("\\Z").next());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
