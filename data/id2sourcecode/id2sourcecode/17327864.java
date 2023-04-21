    public String getToken() {
        log.debug("-------------------get token start-------------------");
        HttpGet get = new HttpGet(Constants.GET_TOKEN_URL);
        String token = null;
        BufferedReader br = null;
        try {
            HttpResponse response = httpclient.execute(get);
            HttpEntity entity = response.getEntity();
            br = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                if (line.indexOf("org.apache.struts.taglib.html.TOKEN") > -1) {
                    token = line;
                }
            }
            if (token != null) {
                int start = token.indexOf("value=\"");
                int end = token.indexOf("\"></div>");
                token = token.substring(start + 7, end);
            } else {
                log.warn("book tikte error, can't get token!");
            }
        } catch (Exception e) {
            log.error(e);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        log.debug("TOKEN = " + token);
        log.debug("-------------------get token end-------------------");
        return token;
    }
