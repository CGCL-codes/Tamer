    protected String getRequestContent(String partOfUrl, HttpMethod method, Map<String, String> paramMap, Map<String, String> headerMap) throws Exception {
        Assertion.notNull(partOfUrl);
        String s = encodeUrl(partOfUrl);
        String urlText = this.config.getProcotol() + "://localhost:" + loader.getPort() + context.getContextPath() + s;
        String params = "";
        if (paramMap != null) {
            StringBuilder builder = new StringBuilder();
            for (Entry<String, String> paramEntry : paramMap.entrySet()) {
                String key = URLEncoder.encode(paramEntry.getKey(), "UTF-8");
                String value = URLEncoder.encode(paramEntry.getValue(), "UTF-8");
                builder.append(key).append("=").append(value).append("&");
            }
            if (0 < builder.length()) {
                builder.setLength(builder.length() - 1);
            }
            params = builder.toString();
        }
        if (method == HttpMethod.GET && "".equals(params) == false) {
            urlText = (urlText.indexOf("?") == -1) ? "?" + params : "&" + params;
        }
        URL url = null;
        if (this.config.getProxyHost() == null && this.config.getProxyPort() == -1) {
            url = new URL(urlText);
        } else {
            new URL(this.config.getProcotol(), this.config.getProxyHost(), this.config.getProxyPort(), urlText);
        }
        HttpURLConnection con = null;
        BufferedReader reader = null;
        try {
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(method.name());
            con.setUseCaches(false);
            con.setDoInput(true);
            con.setDoOutput(true);
            if (headerMap != null) {
                for (Entry<String, String> headerEntry : headerMap.entrySet()) {
                    con.addRequestProperty(headerEntry.getKey(), headerEntry.getValue());
                }
            }
            if (method == HttpMethod.POST) {
                PrintStream ps = new PrintStream(con.getOutputStream());
                ps.print(params);
                ps.close();
            }
            con.connect();
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line = null;
            StringBuilder builder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append(SystemPropertyUtil.LINE_SEP);
            }
            if (0 < builder.length()) {
                builder.setLength(builder.length() - SystemPropertyUtil.LINE_SEP.length());
            }
            return new String(builder);
        } finally {
            if (reader != null) {
                CloseableUtil.close(reader);
            }
            if (con != null) {
                con.disconnect();
            }
        }
    }
