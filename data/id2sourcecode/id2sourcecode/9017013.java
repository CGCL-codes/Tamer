    private String readAbbreviatedLog(File aFile) throws IOException {
        SdlUnsynchronizedCharArrayWriter writer = new SdlUnsynchronizedCharArrayWriter();
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(aFile, "r");
            int headLimit = SdlEngineFactory.getSdlEngineConfig().getIntegerEntry("Logging.Head", IProcessLogger.DEFAULT_HEAD);
            int tailLimit = SdlEngineFactory.getSdlEngineConfig().getIntegerEntry("Logging.Tail", IProcessLogger.DEFAULT_TAIL);
            read(raf, writer, headLimit);
            boolean moved = seekToTail(raf, tailLimit);
            if (moved) {
                writer.write(SNIP);
            }
            read(raf, writer, Integer.MAX_VALUE);
        } finally {
            SdlCloser.close(raf);
        }
        return writer.toString();
    }
