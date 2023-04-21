    public static String getUrlContent(String url) {
        if (StringUtils.isBlank(url)) return "";
        StringBuffer rtn = new StringBuffer();
        HttpURLConnection huc = null;
        BufferedReader br = null;
        try {
            huc = (HttpURLConnection) new URL(url).openConnection();
            huc.connect();
            InputStream stream = huc.getInputStream();
            br = new BufferedReader(new InputStreamReader(stream, "utf-8"));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().length() > 0) {
                    rtn.append(line);
                }
            }
            br.close();
            huc.disconnect();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return rtn.toString();
    }
