    public static TraceFile createTraceFile(URL url) throws IOException {
        return createTraceFile(url.openStream());
    }
