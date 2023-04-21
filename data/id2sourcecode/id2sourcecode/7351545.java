    private static byte[] getRDFBytes(Model model) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Utf8StreamWriter writer = Utf8StreamWriter.getThreadLocal().setOutputStream(out);
        writeRDF(model, writer);
        try {
            writer.close();
        } catch (IOException e) {
        }
        return out.toByteArray();
    }
