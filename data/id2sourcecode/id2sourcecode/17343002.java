    public VzaarTransportResponse uploadToS3(String url, Map<String, String> parameters, File file, UploadProgressCallback callback) throws VzaarException {
        try {
            HttpPost method = new HttpPost(url);
            MultipartEntity parts = new MultipartEntity();
            for (Entry<String, String> entry : parameters.entrySet()) {
                String value = entry.getValue();
                parts.addPart(entry.getKey(), new StringBody(value));
            }
            parts.addPart("file", callback == null ? new FileBody(file) : new ProgressFileBody(file, callback));
            if (debugOut != null) {
                debugOut.write(("\n>> Request URL = " + url + "\n").getBytes());
                debugOut.write((">> Request Method = POST\n").getBytes());
                debugOut.write((">> Request Parameters = " + parameters + "\n").getBytes());
                debugOut.write((">> Request File = " + file.getAbsolutePath() + "\n").getBytes());
            }
            method.setEntity(parts);
            HttpResponse response = client.execute(method);
            String body = EntityUtils.toString(response.getEntity());
            if (body == null) body = "";
            if (debugOut != null) {
                debugOut.write(("<< Response Code = " + response.getStatusLine().getStatusCode() + "\n").getBytes());
                debugOut.write(("<< Response Line = " + response.getStatusLine().getReasonPhrase() + "\n").getBytes());
                debugOut.write(("<< Response Body = \n<< " + body.replaceAll("\n", "\n<< ") + "\n").getBytes());
            }
            return new VzaarTransportResponse(response.getStatusLine().getStatusCode(), response.getStatusLine().getReasonPhrase(), new ByteArrayInputStream(body.getBytes()));
        } catch (Exception e) {
            throw new VzaarException(e.getMessage(), e);
        }
    }
