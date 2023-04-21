    protected void copy(CacheEntry cacheEntry, PrintWriter writer, Iterator ranges, String contentType) throws IOException {
        IOException exception = null;
        while ((exception == null) && (ranges.hasNext())) {
            InputStream resourceInputStream = cacheEntry.resource.streamContent();
            Reader reader;
            if (fileEncoding == null) {
                reader = new InputStreamReader(resourceInputStream);
            } else {
                reader = new InputStreamReader(resourceInputStream, fileEncoding);
            }
            Range currentRange = (Range) ranges.next();
            writer.println();
            writer.println("--" + mimeSeparation);
            if (contentType != null) writer.println("Content-Type: " + contentType);
            writer.println("Content-Range: bytes " + currentRange.start + "-" + currentRange.end + "/" + currentRange.length);
            writer.println();
            exception = copyRange(reader, writer, currentRange.start, currentRange.end);
            reader.close();
        }
        writer.println();
        writer.print("--" + mimeSeparation + "--");
        if (exception != null) throw exception;
    }
