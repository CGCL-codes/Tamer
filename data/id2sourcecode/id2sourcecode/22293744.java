    private void doSearching(final String queryString) {
        try {
            reader = IndexReader.open(indexPath);
            String querieString = queryString.replaceAll("\\\\", "\\\\");
            querieString = querieString.replaceAll(":", "\\\\:");
            querieString = "\"" + querieString + "\"";
            LOG.info("Debug: Query after adaption:" + querieString);
            searcher = new IndexSearcher(indexPath);
            querieString = USER_NAME + ":" + querieString + " " + querieString;
            query = QueryParser.parse(querieString, FIELD_NAME, new StandardAnalyzer());
            LOG.info("Debug: Reader: " + reader);
            LOG.info("Debug: Query parsed: " + query.toString());
            query = query.rewrite(reader);
            LOG.info("Debug: Searching for: " + query.toString(FIELD_NAME) + " in " + indexPath);
            hits = searcher.search(query);
        } catch (IOException e) {
            LOG.debug(e);
        } catch (ParseException e) {
            LOG.debug(e);
        }
    }
