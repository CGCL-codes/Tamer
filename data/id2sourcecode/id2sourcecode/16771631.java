    private void postRequest(HttpExchange a, String path, String query, int port, String host, String schema, Headers headers, HttpHost proxy) throws Exception {
        HttpHost targetHost = new HttpHost(host, port == -1 ? 80 : port, schema);
        Set<String> keys = headers.keySet();
        HttpPost httppost = new HttpPost(path);
        for (String str : keys) {
            if ("Content-Length".equalsIgnoreCase(str)) continue;
            List<String> values = headers.get(str);
            for (String value : values) httppost.addHeader(str, value);
        }
        BasicHttpParams params = new BasicHttpParams();
        if (query != null) {
            String[] split = query.split("&");
            for (String str : split) {
                String[] coppia = str.split("=");
                if (coppia != null && coppia.length == 2) params.setParameter(coppia[0], coppia[1] == null ? "" : coppia[1]);
            }
        }
        params.setBooleanParameter("http.protocol.handle-redirects", false);
        httppost.setParams(params);
        InputStream reqBody = a.getRequestBody();
        BufferedReader reader = new BufferedReader(new InputStreamReader(reqBody));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reqBody.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(line);
        HttpResponse response = null;
        try {
            response = httpclient.execute(targetHost, httppost);
            httpclient.getConnectionManager().closeExpiredConnections();
            int statuscode = response.getStatusLine().getStatusCode();
            if ((statuscode == HttpStatus.SC_MOVED_TEMPORARILY) || (statuscode == HttpStatus.SC_MOVED_PERMANENTLY) || (statuscode == HttpStatus.SC_SEE_OTHER) || (statuscode == HttpStatus.SC_TEMPORARY_REDIRECT)) {
                Header header = response.getFirstHeader("Location");
                if (header != null) {
                    String newuri = header.getValue();
                    if ((newuri == null) || (newuri.equals(""))) {
                        newuri = "/";
                    }
                    System.out.println("Redirect target: " + newuri);
                    HttpGet redirect = new HttpGet(newuri);
                    response.getEntity().consumeContent();
                    response = httpclient.execute(redirect);
                    System.out.println("Redirect: " + response.getStatusLine().toString());
                }
            }
        } catch (Exception eeee) {
            eeee.printStackTrace();
        }
        if (response != null) {
            Header[] array = response.getAllHeaders();
            for (Header hh : array) a.getResponseHeaders().add(hh.getName(), hh.getValue());
            HttpEntity entity = response.getEntity();
            long resultLenght = -1;
            if (entity != null) resultLenght = entity.getContentLength();
            a.sendResponseHeaders(response.getStatusLine().getStatusCode(), resultLenght);
            if (resultLenght != -1) {
                InputStream is = entity.getContent();
                OutputStream ou = a.getResponseBody();
                BufferedOutputStream bos = new BufferedOutputStream(ou);
                int r;
                while ((r = is.read()) != -1) bos.write(r);
                bos.flush();
                bos.close();
                ou.flush();
                ou.close();
            }
            System.out.println("----------------------------------------");
            System.out.println(response.getStatusLine());
            if (entity != null) {
                System.out.println("Response content length: " + entity.getContentLength());
                entity.consumeContent();
            }
        }
        a.getResponseBody().flush();
        a.getResponseBody().close();
        a.close();
    }
