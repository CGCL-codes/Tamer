    private pubfetch.MedlineMap readMedlineMapFromSite() throws IOException {
        String pubmed_fetch_url = "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=pubmed";
        pubfetch.PubmedFetcher fetcher = new pubfetch.PubmedFetcher();
        String url = pubmed_fetch_url + "&id=" + _pid + "&retmode=MEDLINE&rettype=MEDLINE";
        pubfetch.MedlineMap amap = null;
        try {
            InputStream is = (new URL(url)).openStream();
            fetcher.extractMapsFromInputStream(is);
            List documents = fetcher.documents();
            amap = (pubfetch.MedlineMap) documents.get(0);
        } catch (pubfetch.FetchError fe) {
            throw new IOException("fetch error for " + url);
        } catch (MalformedURLException me) {
            throw new IOException("malformed url from :" + url);
        } catch (IOException ie) {
            throw new IOException("couldn't read site:" + url);
        }
        return amap;
    }
