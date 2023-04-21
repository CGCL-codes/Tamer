    public InputStream getInputStream() {
        if ((this.data == null || this.data.equals("")) && this.url != null) {
            URL urlToOpen = null;
            try {
                urlToOpen = this.getURLContent();
                if (urlToOpen != null) {
                    XkinsXMLLoader.addConfigFilesTimeStamp(urlToOpen.getFile());
                    return urlToOpen.openStream();
                }
            } catch (MalformedURLException mue) {
                XkinsLogger.getLogger().error("Error getting content input Stream (MalformedURLException)", mue);
            } catch (IOException io) {
                XkinsLogger.getLogger().error("Error getting content input Stream (IOException)", io);
            }
            return null;
        } else {
            return new ByteArrayInputStream(this.data.getBytes());
        }
    }
