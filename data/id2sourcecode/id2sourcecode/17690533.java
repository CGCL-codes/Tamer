    public boolean saveWikiPage(String pageName, String pageContent, String author) {
        URL url;
        HttpURLConnection connectHttp;
        StringBuffer sb = new StringBuffer();
        String inputLine;
        BufferedReader br;
        String encodedStr;
        PrintStream ps;
        try {
            for (Map.Entry child : dataSet) sb.append(child.getKey() + "=" + child.getValue() + "&");
            url = new URL(getPagepath(pageName));
            connectHttp = (HttpURLConnection) url.openConnection();
            initConnection(connectHttp);
            connectHttp.setRequestMethod("POST");
            connectHttp.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connectHttp.setRequestProperty("Authorization", "Basic " + Base64.encode(user + ":" + password));
            sb.append("pageContent=" + pageContent);
            encodedStr = URLEncoder.encode(sb.toString(), "UTF-8");
            encodedStr = encodedStr.replace("%3D", "=");
            encodedStr = encodedStr.replace("%26", "&");
            ps = GetPrintStream(connectHttp);
            ps.println(encodedStr);
            ps.close();
            br = GetInputStream(connectHttp);
            br.close();
            if (connectHttp.getResponseMessage().equals("OK")) return true;
            connectHttp.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
