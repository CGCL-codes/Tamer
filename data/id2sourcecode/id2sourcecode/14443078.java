    protected String getResponse(HttpUriRequest request) throws IOException {
        synchronized (mHttpClient) {
            HttpResponse httpResponse = mHttpClient.execute(request);
            HttpEntity entity = httpResponse.getEntity();
            if (entity == null) return null;
            InputStream stream = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream), 10000);
            StringBuilder builder = new StringBuilder(10000);
            String str;
            while ((str = reader.readLine()) != null) builder.append(str);
            stream.close();
            entity.consumeContent();
            return builder.toString();
        }
    }
