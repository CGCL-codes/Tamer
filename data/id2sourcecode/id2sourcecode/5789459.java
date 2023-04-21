    public Documents search(String queryString, Integer[] ids, int limit, boolean sort) {
        Query query = null;
        try {
            if (queryString != null && queryString.trim().length() > 0) {
                query = parser.parse(queryString);
            }
            Query idQuery = query;
            if (ids != null) {
                String idString = QueryWrapper.getLuceneIdsQuery(ids);
                idQuery = parser.parse(idString);
            }
            IndexSearcher searcher = null;
            IndexReader reader = null;
            long t1 = System.currentTimeMillis();
            TopDocCollector collector = null;
            TopDocs topDocs = null;
            long t2 = 0;
            if (ids == null || ids.length > limit) {
                searcher = getSearcher(false);
                reader = searcher.getIndexReader();
                int maxDoc = searcher.maxDoc();
                int limitSearch = limit;
                if ((limitSearch < 0) || (limitSearch > maxDoc)) limitSearch = maxDoc;
                collector = new TopDocCollector(limitSearch);
                t2 = System.currentTimeMillis();
                searcher.search(idQuery, collector);
                topDocs = collector.topDocs();
            }
            if (ids != null && (collector == null || collector.getTotalHits() < limit)) {
                searcher = getSearcher(true);
                reader = searcher.getIndexReader();
                int maxDoc = searcher.maxDoc();
                int limitSearch = limit;
                if ((limitSearch < 0) || (limitSearch > maxDoc)) limitSearch = maxDoc;
                t2 = System.currentTimeMillis();
                if (!sort) {
                    collector = new TopDocCollector(limitSearch);
                    searcher.search(idQuery, collector);
                    topDocs = collector.topDocs();
                } else {
                    topDocs = searcher.search(idQuery, null, limitSearch);
                }
            }
            long t3 = System.currentTimeMillis();
            ScoreDoc[] docs = topDocs.scoreDocs;
            long t4 = System.currentTimeMillis();
            if (query != null) {
                query = query.rewrite(reader);
            }
            long t5 = System.currentTimeMillis();
            long ctdt = t2 - t1;
            long st = t3 - t2;
            long tdt = t4 - t3;
            long rwt = t5 - t4;
            log.info("query:" + idQuery.toString() + " original:" + queryString + " time:cs(" + ctdt + ") srch(" + st + ") ftd(" + tdt + ") rewrt(" + rwt + ") count:" + docs.length);
            if (docs.length == 0) {
                return new Documents();
            } else {
                HashMap<String, String[]> copy = new HashMap<String, String[]>(queryFieldSelectors);
                QueryHandle qh = new QueryHandle(reader, docs, query);
                return new Documents(qh, copy, docs.length, st);
            }
        } catch (Exception e) {
            if (e instanceof IOException) log.error("Lucene IO error when performing search.", e); else if (e instanceof ParseException) log.error("Lucene parsing error on \"" + queryString + "\"", e); else {
                log.error("Error executing query", e);
                e.printStackTrace();
            }
            return new Documents();
        }
    }
