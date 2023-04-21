    public static String getBBOXAsString(Configuration config) throws Exception {
        String servWMSurl = config.getWMS().getServerByDefault().getUrl();
        String separator = "?";
        if (StringUtils.contains(servWMSurl, "?")) separator = "&";
        URL url = new URL(servWMSurl + separator + "request=GetCapabilities&version=1.1.1&service=WMS");
        URLConnection urlc = url.openConnection();
        BufferedReader br = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            result.append(line + ls);
        }
        br.close();
        String strbbox = StringUtils.substringBetween(result.toString(), "<LatLonBoundingBox", "/>");
        String bbox = "";
        bbox += StringUtils.substringBetween(strbbox, "minx=\"", "\"") + ",";
        bbox += StringUtils.substringBetween(strbbox, "miny=\"", "\"") + ",";
        bbox += StringUtils.substringBetween(strbbox, "maxx=\"", "\"") + ",";
        bbox += StringUtils.substringBetween(strbbox, "maxy=\"", "\"");
        return bbox;
    }
