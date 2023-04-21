    public static String postForm(String urlString, Map<String, String> formMap) throws IOException, MalformedURLException {
        URL url;
        URLConnection urlConn;
        DataOutputStream printout;
        BufferedReader input;
        StringBuffer urlResponse;
        url = getUrl(urlString);
        if (url == null) {
            return null;
        }
        urlConn = (URLConnection) url.openConnection();
        urlConn.setDoInput(true);
        urlConn.setDoOutput(true);
        urlConn.setUseCaches(false);
        urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        printout = new DataOutputStream(urlConn.getOutputStream());
        if (formMap != null) {
            StringBuffer formContent = new StringBuffer();
            Iterator<String> iter = formMap.keySet().iterator();
            while (iter.hasNext()) {
                String key = (String) iter.next();
                formContent.append(URLEncoder.encode(key, "UTF-8"));
                formContent.append('=');
                if (formMap.get(key) != null) formContent.append(URLEncoder.encode((String) formMap.get(key), "UTF-8"));
                if (iter.hasNext()) formContent.append('&');
            }
            printout.writeBytes(formContent.toString());
            printout.flush();
            printout.close();
        }
        urlResponse = new StringBuffer();
        input = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
        String str;
        while (null != ((str = input.readLine()))) {
            log.debug(str);
            urlResponse.append(str + "\n");
        }
        input.close();
        return urlResponse.toString();
    }
