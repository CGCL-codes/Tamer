    private void copy(ResourceInfo resourceInfo, PrintWriter writer, Enumeration ranges, String contentType) throws IOException {
        IOException exception = null;
        while ((exception == null) && (ranges.hasMoreElements())) {
            InputStream resourceInputStream = resourceInfo.getStream();
            Reader reader = new InputStreamReader(resourceInputStream);
            Range currentRange = (Range) ranges.nextElement();
            writer.println();
            writer.println("--" + mimeSeparation);
            if (contentType != null) {
                writer.println("Content-Type: " + contentType);
            }
            writer.println("Content-Range: bytes " + currentRange.start + "-" + currentRange.end + "/" + currentRange.length);
            writer.println();
            exception = copyRange(reader, writer, currentRange.start, currentRange.end);
            try {
                reader.close();
            } catch (Throwable t) {
                ;
            }
        }
        writer.println();
        writer.print("--" + mimeSeparation + "--");
        if (exception != null) throw exception;
    }
