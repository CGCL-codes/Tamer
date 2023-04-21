    public final boolean isUpToDate(final URL id) {
        String urlStr = id.toString();
        urlStr = urlStr.substring(0, urlStr.length() - OFFSET) + "dateLastChanged";
        StringWriter out = new StringWriter();
        String timestamp = "";
        LOGGER.finer("Using url " + urlStr);
        try {
            URL url = new URL(urlStr);
            InputStream in = new BufferedInputStream(url.openStream());
            int j;
            while ((j = in.read()) != -1) {
                out.write(j);
            }
            in.close();
            out.flush();
            out.close();
            timestamp = out.toString().trim();
            LOGGER.finer("Got timestamp for model " + id + ": " + timestamp);
            if (this.timestamps.containsKey(id)) {
                if (timestamp.compareTo(this.timestamps.get(id)) > 0) {
                    this.timestamps.put(id, timestamp);
                    return false;
                }
            } else {
                this.timestamps.put(id, timestamp);
            }
        } catch (Exception e) {
            LOGGER.warning("Couldn't fetch timestamp for model " + id);
        }
        return true;
    }
