    public void connect() throws IOException, JSONException {
        String params = "";
        if (Request_TYPE.POST == type) {
            String surl = url.toString();
            int flag = surl.indexOf("?");
            String method = surl.substring(0, flag);
            params = surl.substring(flag + 1, surl.length());
            url = new URL(method);
        }
        conn = (HttpURLConnection) url.openConnection();
        if (null != referer) conn.setRequestProperty("Referer", referer);
        conn.setRequestProperty("Host", url.getHost());
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:2.0.1) Gecko/20100101 Firefox/4.0.1");
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("Cookie", cookieManager.getCookies());
        conn.setConnectTimeout(3000);
        switch(type) {
            case GET:
                conn.setRequestMethod("GET");
                conn.connect();
                break;
            case POST:
                if (!"".equals(params)) {
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    OutputStream os = conn.getOutputStream();
                    OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
                    osw.write(params);
                    osw.flush();
                    osw.close();
                    os.close();
                }
                break;
        }
        List<String> cookies = conn.getHeaderFields().get("Set-Cookie");
        cookieManager.setCookies(cookies);
        response = new Response();
        Map<String, List<String>> fields = conn.getHeaderFields();
        List<String> types = fields.get("Content-Type");
        if (null == types) throw new IOException();
        String scontentType = types.get(0);
        if (scontentType.indexOf("plain") != -1 || scontentType.indexOf("json") != -1) {
            InputStream ins = conn.getInputStream();
            BufferedInputStream instream = new BufferedInputStream(ins);
            ByteArrayBuffer baf = new ByteArrayBuffer(50);
            int current = 0;
            while ((current = instream.read()) != -1) {
                baf.append((byte) current);
            }
            bytes = baf.toByteArray();
            instream.close();
            ins.close();
            response.setRType(Response_TYPE.JSON);
            String s = new String(bytes);
            JSONObject json = new JSONObject(s);
            response.setJsonObj(json);
            response.setText(s);
        } else if (scontentType.indexOf("image") != -1) {
            response.setRType(Response_TYPE.STREAM);
            response.setStream(conn.getInputStream());
        } else if (scontentType.indexOf("html") != -1 || scontentType.indexOf("javascript") != -1) {
            InputStream ins = conn.getInputStream();
            BufferedInputStream instream = new BufferedInputStream(ins);
            ByteArrayBuffer baf = new ByteArrayBuffer(50);
            int current = 0;
            while ((current = instream.read()) != -1) {
                baf.append((byte) current);
            }
            bytes = baf.toByteArray();
            instream.close();
            ins.close();
            response.setRType(Response_TYPE.TEXT);
            String s = new String(bytes);
            response.setText(s);
        }
    }
