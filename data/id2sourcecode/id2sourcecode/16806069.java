    public Double calculateSimilarity(String term1, String term2) throws JSONException, IOException, SQLException {
        term1 = term1.trim();
        term2 = term2.trim();
        if (term1.equals("") || term2.equals("") || term1.equals("\"\"") || term2.equals("\"\"")) return 0.0;
        Double similarity = 0.0;
        long TOTAL_DOC_COUNT = 0;
        long WIKI_PAGE_COUNT = 0;
        long NIH_PAGE_COUNT = 0;
        long WORDNET_PAGE_COUNT = 0;
        switch(searchEngine) {
            case Google:
                TOTAL_DOC_COUNT = TOTAL_DOC_GOOGLE;
                WIKI_PAGE_COUNT = WIKI_PAGE_GOOGLE;
                NIH_PAGE_COUNT = NIH_PAGE_GOOGLE;
                WORDNET_PAGE_COUNT = WIKI_PAGE_GOOGLE;
                break;
            case Yahoo:
                TOTAL_DOC_COUNT = TOTAL_DOC_YAHOO;
                WIKI_PAGE_COUNT = WIKI_PAGE_YAHOO;
                NIH_PAGE_COUNT = NIH_PAGE_YAHOO;
                WORDNET_PAGE_COUNT = WIKI_PAGE_YAHOO;
                break;
        }
        long total_pages = 0;
        switch(corpus) {
            case NIH:
                total_pages = NIH_PAGE_COUNT;
                break;
            case WIKI:
                total_pages = WIKI_PAGE_COUNT;
                break;
            case Unconstrained:
                total_pages = TOTAL_DOC_COUNT;
                break;
            case WORDNET:
                total_pages = WORDNET_PAGE_COUNT;
                break;
        }
        double x_count = numResultsFromWeb(term1, 1);
        double y_count = numResultsFromWeb(term2, 1);
        double xy_count = numResultsFromWeb(term1 + " AND " + term2, 1);
        double px = x_count / total_pages;
        double py = y_count / total_pages;
        double pxy = xy_count / total_pages;
        switch(similarityMethod) {
            case PMI:
                similarity = -Math.log(pxy / (px * py)) / Math.log(pxy);
                similarity = (similarity + 1) / 2;
                break;
            case NGD:
                double log_x_count = Math.log(x_count);
                double log_y_count = Math.log(y_count);
                double log_xy_count = Math.log(xy_count);
                similarity = Math.max(log_x_count, log_y_count) - log_xy_count;
                similarity = 1 - (similarity / (Math.log(total_pages) - Math.min(log_x_count, log_y_count)));
                break;
        }
        if (similarity.isInfinite() || similarity.isNaN()) similarity = 0.0;
        Util.log(term1 + " - " + term2 + " = " + similarity, 1);
        return similarity;
    }
