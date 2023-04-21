    private void close(IndexReader reader, Directory directory, IndexWriter writer, IndexSearcher searcher) throws IOException {
        if (directory != null && !useRamIndex) directory.close();
        if (reader != null) reader.close();
        if (writer != null) writer.close();
        if (searcher != null) searcher.close();
    }
