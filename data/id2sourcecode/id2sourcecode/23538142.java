    public static byte[] downloadImageData(final URL imageUrl) {
        HttpClient downloadClient = null;
        try {
            URI imageUri = new URI(imageUrl.toExternalForm());
            downloadClient = createHttpClient(imageUri);
            HttpGet downloadMethod = new HttpGet(imageUri);
            HttpResponse response = downloadClient.execute(downloadMethod);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != OK_STATUS_CODE) {
                IStatus status = getStatuses().create(WARNING, STATUS_CODE_ERROR_HTTP_STATUS, null, statusCode, imageUrl);
                log(status);
                return null;
            }
            BufferedInputStream is = null;
            ByteArrayOutputStream os = null;
            try {
                is = new BufferedInputStream(response.getEntity().getContent());
                os = new ByteArrayOutputStream();
                byte[] buffer = new byte[BUFFER_SIZE];
                int rbytes = 0;
                while ((rbytes = is.read(buffer)) != -1) {
                    os.write(buffer, 0, rbytes);
                }
                return os.toByteArray();
            } finally {
                if (is != null) {
                    is.close();
                }
                if (os != null) {
                    os.close();
                }
            }
        } catch (IOException ioe) {
            IStatus status = getStatuses().create(WARNING, STATUS_CODE_ERROR_DOWNLOAD, ioe, imageUrl);
            log(status);
        } catch (URISyntaxException use) {
            IStatus status = getStatuses().create(WARNING, STATUS_CODE_ERROR_DOWNLOAD, use, imageUrl);
            log(status);
        } finally {
            if (downloadClient != null && downloadClient.getConnectionManager() != null) {
                downloadClient.getConnectionManager().shutdown();
            }
        }
        return null;
    }
