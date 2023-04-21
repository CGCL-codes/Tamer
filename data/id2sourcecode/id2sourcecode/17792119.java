    public void delete(String path) throws IOException {
        if (webdavResource.deleteMethod(webdavResource.getPath() + "/" + path)) {
            log.info(path + " deleted from WebDAV " + this.httpURL.toString());
        } else if (webdavResource.getStatusCode() == HttpStatus.SC_METHOD_NOT_ALLOWED) {
            URL url = new URL(this.httpURL.toString() + path + "?delete");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setUseCaches(false);
            if (connection.getResponseCode() == 200) {
                log.info(path + " deleted from HTTP servlet " + this.httpURL.toString());
                log.debug(getResponse(connection.getInputStream()));
            } else {
                throw new IOException(path + " could not be deleted from HTTP servlet " + this.httpURL.toString() + " returned " + getResponse(connection.getInputStream()));
            }
        } else {
            throw new IOException("Deleting of " + path + " from " + this.httpURL.toString() + " failed: " + webdavResource.getStatusMessage());
        }
    }
