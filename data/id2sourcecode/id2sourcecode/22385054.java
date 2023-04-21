    public static List URLGet(String strUrl, Map map) throws IOException {
        String strtTotalURL = "";
        List result = new ArrayList();
        if (strtTotalURL.indexOf("?") == -1) {
            strtTotalURL = strUrl + "?" + getUrl(map);
        } else {
            strtTotalURL = strUrl + "&" + getUrl(map);
        }
        log.debug("strtTotalURL:" + strtTotalURL);
        URL url = new URL(strtTotalURL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"), SIZE);
        while (true) {
            String line = in.readLine();
            if (line == null) {
                break;
            } else {
                result.add(line);
            }
        }
        in.close();
        return (result);
    }
