    protected static synchronized String getUrlContent(String url) throws ApiException {
        if (sUserAgent == null) {
            throw new ApiException("User-Agent string must be prepared");
        }
        HttpClient client = new DefaultHttpClient();
        url = "http://ru.windfinder.com/report/stavropol_shpakovskoye_airport";
        HttpGet request = new HttpGet(url);
        Log.d("", "Bor url:" + url);
        request.setHeader("User-Agent", sUserAgent);
        try {
            HttpResponse response = client.execute(request);
            StatusLine status = response.getStatusLine();
            if (status.getStatusCode() != HTTP_STATUS_OK) {
                throw new ApiException("Invalid response from server: " + status.toString());
            }
            HttpEntity entity = response.getEntity();
            InputStream inputStream = entity.getContent();
            ByteArrayOutputStream content = new ByteArrayOutputStream();
            int readBytes = 0;
            while ((readBytes = inputStream.read(sBuffer)) != -1) {
                content.write(sBuffer, 0, readBytes);
            }
            Log.d("", "Bor content:" + content);
            String res = "";
            String Res1 = "";
            String Res2 = "";
            String res1 = "";
            String Res = "";
            StringTokenizer st = new StringTokenizer(content.toString(), "\n");
            boolean recRes = false;
            int recResInt = 0;
            while (st.hasMoreTokens()) {
                res = st.nextToken();
                if (recRes) {
                    recResInt++;
                    if (recResInt == 2) {
                        Log.d("", "Bor TRUE result:" + res);
                        Res = res;
                        StringTokenizer st1 = new StringTokenizer(Res, ">");
                        while (st1.hasMoreTokens()) {
                            res1 = st1.nextToken();
                            if (res1.indexOf("color") > 0) {
                                if (Res1.equals("")) {
                                    Res1 = st1.nextToken();
                                    Res1 = Res1.replace("<", "");
                                    Res1 = Res1.replace("/", "");
                                    Res1 = Res1.replace("f", "");
                                    Res1 = Res1.replace("o", "");
                                    Res1 = Res1.replace("n", "");
                                    Res1 = Res1.replace("t", "");
                                } else {
                                    Res2 = st1.nextToken();
                                    Res2 = Res2.replace("<", "");
                                    Res2 = Res2.replace("/", "");
                                    Res2 = Res2.replace("f", "");
                                    Res2 = Res2.replace("o", "");
                                    Res2 = Res2.replace("n", "");
                                    Res2 = Res2.replace("t", "");
                                }
                            }
                        }
                        recRes = false;
                    }
                }
                if (res.indexOf("(Knots)") > 0) {
                    Log.d("", "Bor result:" + res);
                    recRes = true;
                }
            }
            String t = "{\"query\":{\"pages\":{\"270852\":{\"revisions\":[{\"*\":\"{{wotd|Wind of Stavropol|(Knots)|(Knots)" + Res1 + " (max:" + Res2 + ")|May|17}}\"}]}}}}";
            return t;
        } catch (IOException e) {
            throw new ApiException("Problem communicating with API", e);
        }
    }
