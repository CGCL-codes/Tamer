    void download() throws DownloaderException {
        final HttpClient client = new DefaultHttpClient();
        try {
            final FileOutputStream fos = this.context.openFileOutput(APK_FILENAME, Context.MODE_WORLD_READABLE);
            final HttpResponse response = client.execute(new HttpGet(this.url));
            if (response.getStatusLine().getStatusCode() < 200 || response.getStatusLine().getStatusCode() >= 400) {
                throw new DownloaderException("Invalid status code downloading application: " + response.getStatusLine().getReasonPhrase());
            }
            downloadFile(response, fos);
            fos.close();
        } catch (ClientProtocolException e) {
            throw new DownloaderException(e);
        } catch (IOException e) {
            throw new DownloaderException(e);
        }
    }
