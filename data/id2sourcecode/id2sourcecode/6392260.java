    public InputStream doPost(String url, Map<String, String> data, String referer, String encoding) {
        HttpPost post = null;
        try {
            post = new HttpPost(url);
            setHeaders(post, referer);
            post.addHeader(CONTENT_TYPE_KEY, CONTENT_TYPE_VAL);
            if (!CommonUtil.isEmpty(data)) {
                List<NameValuePair> list = Lists.getList(data.size());
                for (Map.Entry<String, String> entry : data.entrySet()) {
                    list.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
                post.setEntity(new UrlEncodedFormEntity(list, encoding));
            }
            HttpResponse response = execute(post, context);
            updateCurrentUrl(post);
            if (response.getStatusLine().getStatusCode() == 302) {
                return doPost(getRedirectHandler().getLocationURI(response, context).toString(), data, referer);
            } else {
                return response.getEntity().getContent();
            }
        } catch (Exception e) {
            throw new CustomRuntimeException();
        } finally {
            getConnectionManager().closeExpiredConnections();
        }
    }
