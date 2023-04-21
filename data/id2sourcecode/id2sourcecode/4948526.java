    public static String compressJavascript(String javascript) {
        Compressor compressor = CompressorLocator.getCompressor();
        if (compressor != null) {
            try {
                StringReader reader = new StringReader(javascript);
                StringWriter writer = new StringWriter();
                compressor.compress(reader, writer);
                return writer.toString();
            } catch (CompressorException x) {
                LOG.error("Failed to compress inline javascript", x);
            }
        } else {
            LOG.warn("No Javascript compressor found");
        }
        return javascript;
    }
