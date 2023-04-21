    public static String executeGet(String urlStr) throws IOException {
        int index = urlStr.indexOf("?");
        if (index != -1) {
            String firstHalf = urlStr.substring(0, urlStr.indexOf('?') + 1);
            String query = urlStr.substring(index + 1);
            StringBuffer encodedQuery = new StringBuffer();
            StringTokenizer tokens = new StringTokenizer(query, "&");
            while (tokens.hasMoreTokens()) {
                String paramNVal = tokens.nextToken();
                StringTokenizer tokens2 = new StringTokenizer(paramNVal, "=");
                while (tokens2.hasMoreTokens()) {
                    String param = tokens2.nextToken();
                    String val = tokens2.nextToken();
                    try {
                        val = URLEncoder.encode(val, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    encodedQuery.append(param + "=" + val);
                }
                if (tokens.hasMoreTokens()) {
                    encodedQuery.append("&");
                }
            }
            urlStr = firstHalf + encodedQuery;
        }
        StringBuffer result = new StringBuffer();
        URL url = new URL(urlStr);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            result.append(inputLine);
        }
        in.close();
        connection.disconnect();
        return result.toString();
    }
