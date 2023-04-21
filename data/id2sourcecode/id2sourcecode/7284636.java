    public static void partialRead(PrintWriter writer, String filename) throws IOException {
        long before = getMemoryUse();
        PdfReader reader = new PdfReader(new RandomAccessFileOrArray(filename), null);
        reader.getNumberOfPages();
        writer.println(String.format("Memory used by partial read: %d", getMemoryUse() - before));
        writer.flush();
    }
