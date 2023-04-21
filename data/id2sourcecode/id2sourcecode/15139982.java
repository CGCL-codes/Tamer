    private void fetchBatchPubmedDocuments(List ids) throws FetchError {
        String url = pubmed_fetch_url + "&id=" + join(",", ids) + "&retmode=MEDLINE&rettype=MEDLINE";
        Log.getLogger(this.getClass()).debug(url);
        try {
            InputStream is = (new URL(url)).openStream();
            extractMapsFromInputStream(is);
        } catch (Exception e) {
            throw new FetchError(e);
        }
    }
