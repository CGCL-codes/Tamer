    public String loadWikiPage(String pageName) {
        URL url;
        HttpURLConnection connectHttp;
        StringBuffer sb = new StringBuffer();
        String inputLine;
        BufferedReader br;
        try {
            formDataMap.clear();
            url = new URL(getPagepath(pageName) + "?edit");
            connectHttp = (HttpURLConnection) url.openConnection();
            initConnection(connectHttp);
            connectHttp.setRequestMethod("GET");
            connectHttp.setRequestProperty("Content-Type", "text/html");
            connectHttp.connect();
            br = GetInputStream(connectHttp);
            do {
                inputLine = br.readLine();
                sb.append(inputLine).append("\r\n");
            } while (inputLine != null);
            br.close();
            connectHttp.disconnect();
            if (sb.length() != 0) {
                getTextAreaContent(sb);
                getFormData(sb.toString());
                dataSet = formDataMap.entrySet();
                return textareaContent;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
