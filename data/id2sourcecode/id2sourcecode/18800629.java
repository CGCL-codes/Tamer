    @Override
    public UniProtEntry[] getUniProtEntries(final String query) throws ServerException {
        InputStream is = null;
        try {
            final URL url = new URL("http://www.uniprot.org/uniprot/?query=" + query + "&format=xml");
            final URLConnection urlConnection = url.openConnection();
            if (urlConnection instanceof HttpURLConnection) {
                final int responseCode = ((HttpURLConnection) urlConnection).getResponseCode();
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    throw new IllegalArgumentException("Unable to find UniProt entries for query: " + query);
                }
            }
            is = url.openStream();
            return UniProtUtils.getUniProtEntry(is);
        } catch (Exception e) {
            throw new ServerException(e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    throw new ServerException(e);
                }
            }
        }
    }
