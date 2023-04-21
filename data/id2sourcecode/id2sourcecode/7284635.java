    public static void fullRead(PrintWriter writer, String filename) throws IOException {
        long before = getMemoryUse();
        PdfReader reader = new PdfReader(filename);
        reader.getNumberOfPages();
        writer.println(String.format("Memory used by full read: %d", getMemoryUse() - before));
        writer.flush();
    }
