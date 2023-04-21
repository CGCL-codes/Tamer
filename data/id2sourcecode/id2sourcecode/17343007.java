    private VzaarTransportResponse excuteMethod(HttpUriRequest method) throws OAuthMessageSignerException, OAuthExpectationFailedException, IOException, HttpException {
        if (consumer != null) {
            consumer.sign(method);
        }
        HttpResponse response = client.execute(method, state);
        InputStream in = null;
        if (debugOut != null) {
            String body = EntityUtils.toString(response.getEntity());
            in = new ByteArrayInputStream(body.getBytes());
            debugOut.write(("<< Response Code = " + response.getStatusLine().getStatusCode() + "\n").getBytes());
            debugOut.write(("<< Response Line = " + response.getStatusLine().getReasonPhrase() + "\n").getBytes());
            debugOut.write(("<< Response Body = \n<< " + body.replaceAll("\n", "\n<< ") + "\n").getBytes());
        } else {
            in = response.getEntity().getContent();
        }
        return new VzaarTransportResponse(response.getStatusLine().getStatusCode(), response.getStatusLine().getReasonPhrase(), in);
    }
