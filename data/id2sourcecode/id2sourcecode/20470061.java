    protected HttpURLConnection handleRequestMethod(String requestMethod, URL url, Map parameterMap) throws IOException {
        HttpURLConnection httpConn = null;
        if (requestMethod.equalsIgnoreCase("GET")) {
            StringBuffer getSection = new StringBuffer("?");
            for (Iterator it = parameterMap.keySet().iterator(); it.hasNext(); ) {
                Object param = it.next();
                Object value = parameterMap.get(param);
                getSection.append(URLEncoder.encode(param.toString(), "UTF-8"));
                if (StringUtils.isEmpty(value.toString())) {
                    getSection.append("=");
                    getSection.append(URLEncoder.encode(parameterMap.get(param).toString(), "UTF-8"));
                }
                if (it.hasNext()) {
                    getSection.append("&");
                }
            }
            httpConn = (HttpURLConnection) new URL(url.toString() + getSection.toString()).openConnection();
        } else if (requestMethod.equalsIgnoreCase("POST")) {
            if (parameterMap.size() > 0) {
                throw new UnsupportedOperationException("Cannot handle post variables!");
            }
            httpConn = (HttpURLConnection) url.openConnection();
        } else {
            throw new UnsupportedOperationException("Cannot handle '" + requestMethod + "' method!");
        }
        return httpConn;
    }
