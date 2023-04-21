    private void close(final IndexWriter writer, final IndexReader reader, final IndexSearcher searcher) {
        if (writer != null) {
            try {
                if (OPTIMIZE) {
                    writer.optimize();
                }
            } catch (Exception e) {
                LOGGER.error("failed to optimize", e);
            } finally {
                try {
                    writer.close();
                } catch (Exception e) {
                    LOGGER.error("failed to close", e);
                }
            }
        }
        if (reader != null) {
            try {
                reader.close();
            } catch (Exception e) {
                LOGGER.error("failed to close reader", e);
            }
        }
        if (searcher != null) {
            try {
                searcher.close();
            } catch (Exception e) {
                LOGGER.error("failed to close searcher", e);
            }
        }
    }
