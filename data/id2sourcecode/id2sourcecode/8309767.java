    public File loadModel() throws ServiceErrorException, UnexpectedException, NetworkException, StorageException {
        File destination = null;
        FileOutputStream outputStream = null;
        InputStream contentStream = null;
        try {
            destination = LocalStorage.createTempFile();
            HttpGet httpGet = new HttpGet(this.serviceUrl);
            BasicHttpParams params = new BasicHttpParams();
            params.setParameter("op", ActionCode.LoadModel);
            httpGet.setParams(params);
            HttpResponse response = this.httpClient.execute(httpGet, this.localContext);
            HttpEntity entity = response.getEntity();
            String contentType = entity.getContentType().getValue();
            contentStream = entity.getContent();
            if (!contentType.equals("application/vnd.monet.model")) throw new UnexpectedException("Unexpected content mimeType received from server.");
            outputStream = new FileOutputStream(destination);
            copyStream(contentStream, outputStream);
        } catch (ClientProtocolException e) {
            throw new NetworkException(e.getMessage(), e);
        } catch (IOException e) {
            throw new NetworkException(e.getMessage(), e);
        } finally {
            StreamHelper.close(outputStream);
        }
        return destination;
    }
