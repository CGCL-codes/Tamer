    public Element exec(Element params, ServiceContext context) throws Exception {
        String sUrl = params.getChildText(URL_PARAM_NAME);
        if (sUrl == null) sUrl = configUrl;
        if (sUrl == null) throw new IllegalArgumentException("The '" + URL_PARAM_NAME + "' configuration parameter is missing");
        boolean first = new URL(sUrl).getQuery() == null;
        StringBuffer sb = new StringBuffer(sUrl);
        for (Iterator iter = params.getChildren().iterator(); iter.hasNext(); ) {
            Element child = (Element) iter.next();
            if (child.getName().equals(URL_PARAM_NAME)) continue;
            if (first) {
                first = false;
                sb.append("?");
            } else sb.append("&");
            sb.append(child.getName()).append("=").append(URLEncoder.encode(child.getText(), "UTF-8"));
        }
        URL url = new URL(sb.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        InputStream input = conn.getInputStream();
        return Xml.loadStream(input);
    }
