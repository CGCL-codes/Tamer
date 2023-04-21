    @SuppressWarnings("unchecked")
    public <T> T execute(Request request) throws ConnectionException {
        try {
            BasicNameValuePair actionParam = new BasicNameValuePair("op", request.getAction().name());
            HttpPost httpPost = new HttpPost(this.serviceUrl + "?" + actionParam.toString());
            ByteArrayOutputStream requestOutputStream = new ByteArrayOutputStream();
            GZIPOutputStream zipStream = new GZIPOutputStream(requestOutputStream);
            Persister persister = new Persister();
            persister.write(request, zipStream, "UTF-8");
            StreamHelper.close(zipStream);
            StreamHelper.close(requestOutputStream);
            ByteArrayInputStream instream = new ByteArrayInputStream(requestOutputStream.toByteArray());
            httpPost.setHeader("Content-Encoding", "gzip");
            httpPost.setEntity(new InputStreamEntity(instream, instream.available()));
            HttpResponse httpResponse = this.httpClient.execute(httpPost, this.localContext);
            HttpEntity entity = httpResponse.getEntity();
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            String reason = httpResponse.getStatusLine().getReasonPhrase();
            if (statusCode >= 200 && statusCode < 300 && entity.getContentEncoding().getValue().equals("gzip")) {
                GZIPInputStream responseStream = new GZIPInputStream(entity.getContent());
                try {
                    Response response = persister.read(Response.class, responseStream);
                    if (response.isError()) {
                        throw new ActionException(response.getError());
                    }
                    return (T) response.getResult();
                } finally {
                    StreamHelper.close(responseStream);
                }
            } else {
                throw new ConnectionException(statusCode + " " + reason);
            }
        } catch (Exception e) {
            Log.e("MonetMobile", "Error:\n\n" + e.getMessage());
            throw new ConnectionException(e.getMessage(), e);
        }
    }
