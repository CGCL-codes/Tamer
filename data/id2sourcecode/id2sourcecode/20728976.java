    private static Set<Integer> fromTerms(String term, int max_return) throws SAXException, IOException {
        QueryKeyHandler.FetchSet handler = new QueryKeyHandler.FetchSet();
        URL url = new URL("http://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=pubmed&term=" + URLEncoder.encode(term, "UTF-8") + "&retstart=0&retmax=" + max_return + "&usehistory=y&retmode=xml&email=plindenbaum_at_yahoo.fr&tool=meshfreqs");
        InputStream in = url.openStream();
        newSAXParser().parse(in, handler);
        in.close();
        return handler.getPMID();
    }
