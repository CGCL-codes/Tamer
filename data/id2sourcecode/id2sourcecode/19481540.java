    private String constructErrorMessage(final String msg) {
        final StringBuilder sb = new StringBuilder(msg);
        sb.append("; BinaryCodec in ");
        sb.append(isWriting ? "write" : "read");
        sb.append("mode; ");
        final String filename = isWriting ? outputFileName : inputFileName;
        if (filename != null) {
            sb.append("file: ");
            sb.append(filename);
        } else {
            sb.append("streamed file (filename not available)");
        }
        return sb.toString();
    }
