    @Override
    protected int transferFile(NioSession session, FileRegion region, int length) throws Exception {
        try {
            return (int) region.getFileChannel().transferTo(region.getPosition(), length, session.getChannel());
        } catch (IOException e) {
            String message = e.getMessage();
            if (message != null && message.contains("temporarily unavailable")) {
                return 0;
            }
            throw e;
        }
    }
