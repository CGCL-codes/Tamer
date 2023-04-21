    private String getResource(String urlSpec) {
        InputStream in = null;
        String result = null;
        try {
            URL url = new URL(urlSpec);
            URLConnection conn = url.openConnection();
            conn.connect();
            in = conn.getInputStream();
            result = new Scanner(in).useDelimiter("\\Z").next();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
        return result;
    }
