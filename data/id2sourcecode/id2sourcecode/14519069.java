    @Override
    public synchronized int removeIndexedDocuments(final String database, String secondaryIdName, final Collection<Pair<String, String>> primaryAndSecondaryIds, int olderThanDays) {
        IndexWriter writer = null;
        IndexReader reader = null;
        IndexSearcher searcher = null;
        final BooleanQuery topQuery = new BooleanQuery();
        int before = 0;
        int after = 0;
        int failed = 0;
        final Timer timer = Metric.newTimer("IndexerImpl.index");
        try {
            Tuple tuple = open(null);
            writer = tuple.first();
            reader = tuple.second();
            searcher = tuple.third();
            Date startDate = new Date(0L);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(TimeUtils.getCurrentTime());
            calendar.add(Calendar.DATE, -olderThanDays);
            Date endDate = calendar.getTime();
            if (primaryAndSecondaryIds != null && primaryAndSecondaryIds.size() > 0) {
                for (Pair<String, String> primaryAndSecondaryId : primaryAndSecondaryIds) {
                    try {
                        final BooleanQuery booleanQuery = new BooleanQuery();
                        Filter filter = null;
                        booleanQuery.add(new TermQuery(new Term(Document.ID, primaryAndSecondaryId.getFirst())), Occur.MUST);
                        if (!GenericValidator.isBlankOrNull(primaryAndSecondaryId.getSecond())) {
                            booleanQuery.add(new TermQuery(new Term(Document.DATABASE, database)), Occur.MUST);
                            booleanQuery.add(new TermQuery(new Term(secondaryIdName, primaryAndSecondaryId.getSecond())), Occur.MUST);
                        }
                        if (olderThanDays > 0) {
                            final String indexStartDateRange = DateTools.dateToString(startDate, DateTools.Resolution.DAY);
                            final String indexEndDateRange = DateTools.dateToString(endDate, DateTools.Resolution.DAY);
                            filter = new TermRangeFilter("indexDate", indexStartDateRange, indexEndDateRange, true, true);
                        }
                        Query q = filter == null ? booleanQuery : new FilteredQuery(booleanQuery, filter);
                        topQuery.add(q, Occur.SHOULD);
                    } catch (Exception e) {
                        LOGGER.error("Failed to remove index for " + primaryAndSecondaryId, e);
                        failed++;
                    }
                }
            } else if (olderThanDays > 0) {
                final BooleanQuery booleanQuery = new BooleanQuery();
                Filter filter = null;
                booleanQuery.add(new TermQuery(new Term(Document.DATABASE, database)), Occur.MUST);
                final String indexStartDateRange = DateTools.dateToString(startDate, DateTools.Resolution.DAY);
                final String indexEndDateRange = DateTools.dateToString(endDate, DateTools.Resolution.DAY);
                filter = new TermRangeFilter("indexDate", indexStartDateRange, indexEndDateRange, true, true);
                Query q = new FilteredQuery(booleanQuery, filter);
                topQuery.add(q, Occur.SHOULD);
            }
            before = searcher.search(topQuery, 1).totalHits;
            writer.deleteDocuments(topQuery);
        } catch (final Exception e) {
            LOGGER.error("Faield to remove index", e);
        } finally {
            timer.stop("succeeded removing index " + (primaryAndSecondaryIds != null ? primaryAndSecondaryIds.size() : 0) + ", before " + before + ", after " + after);
            close(writer, reader, searcher);
            after = getCount(topQuery);
            CacheFlusher.getInstance().flushCaches();
        }
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("#### Removed index documents " + (primaryAndSecondaryIds != null ? primaryAndSecondaryIds.size() : 0) + ", before " + before + ", after " + after + ", failed " + failed);
        }
        return before - after;
    }
