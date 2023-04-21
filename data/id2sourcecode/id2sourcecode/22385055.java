    public static List URLPost(String strUrl, Map map) throws IOException {
        String content = "";
        content = getUrl(map);
        String totalURL = null;
        if (strUrl.indexOf("?") == -1) {
            totalURL = strUrl + "?" + content;
        } else {
            totalURL = strUrl + "&" + content;
        }
        URL url = new URL(strUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setAllowUserInteraction(false);
        con.setUseCaches(false);
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=GBK");
        BufferedWriter bout = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
        bout.write(content);
        bout.flush();
        bout.close();
        BufferedReader bin = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"), SIZE);
        List result = new ArrayList();
        while (true) {
            String line = bin.readLine();
            if (line == null) {
                break;
            } else {
                result.add(line);
            }
        }
        return (result);
    }
