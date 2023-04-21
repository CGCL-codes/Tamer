    public Resource fetch(final boolean lastFetch) throws Exception {
        if (!this.canCrawl()) {
            this.fetchedResource.addInnerURLs(this.getInnerURLs(null));
            return this.fetchedResource;
        }
        URLConnection urlConnection = null;
        try {
            urlConnection = this.fetchedResource.getURL().openConnection();
            if (lastFetch) this.lastFetch(urlConnection);
            urlConnection.setConnectTimeout(Fetcher.connectTimeout);
            urlConnection.setReadTimeout(Fetcher.fetchTimeout);
            urlConnection.connect();
            this.correctURL(urlConnection);
            final InputStream copiedInputStream = copyInputStream(urlConnection.getInputStream());
            final String charset = this.getCharset(copiedInputStream);
            this.fetchedResource.setInputStream(copiedInputStream);
            this.fetchedResource.setCharset(charset);
            this.fetchedResource.addInnerURLs(this.getInnerURLs(charset));
            this.fetchedResource.setContentType(urlConnection.getContentType());
            return this.fetchedResource;
        } catch (final Exception e) {
            this.treatFetchException(urlConnection, e);
            throw e;
        } finally {
            this.closeConnectionToResource(urlConnection);
        }
    }
