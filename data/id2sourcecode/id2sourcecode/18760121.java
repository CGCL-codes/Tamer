    private static JadProperties loadJadProperties(String urlString) throws IOException {
        JadProperties properties = new JadProperties();
        URL url = new URL(urlString);
        if (url.getUserInfo() == null) {
            properties.load(url.openStream());
        } else {
            URLConnection cn = url.openConnection();
            String userInfo = new String(Base64Coder.encode(url.getUserInfo().getBytes("UTF-8")));
            cn.setRequestProperty("Authorization", "Basic " + userInfo);
            properties.load(cn.getInputStream());
        }
        return properties;
    }
