    private HttpPost openConnection(final String url, final NameValuePairBuilder nameValuePairBuilder) throws UnsupportedEncodingException {
        HttpPost httpost = new HttpPost(url);
        httpost.setEntity(new UrlEncodedFormEntity(nameValuePairBuilder.build(), HTTP.UTF_8));
        return httpost;
    }
