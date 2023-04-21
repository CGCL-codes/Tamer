    public Writer getWriter() throws IOException {
        if (DiskCacheUtils.isRemote(this.getURL())) {
            throw new IOException("Cannot write to remote URLs!");
        }
        if (DiskCacheUtils.isLocal(this.getURL())) {
            URL url = new URL(this.getURL());
            if (url.getProtocol().equalsIgnoreCase("http")) {
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("PUT");
                return new HttpURLWriter(conn);
            } else {
                File file = new File(url.getFile());
                file.getParentFile().mkdirs();
                return new FileURLWriter(file);
            }
        }
        throw new IOException(this.getURL() + ": is not local or remote");
    }
