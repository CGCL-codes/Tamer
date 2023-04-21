    public static void report(Writer out, IReportable base, final InputStream style, final IHeaderMatcher matcher, final ILicenseFamily[] approvedLicenseNames) throws IOException, TransformerConfigurationException, FileNotFoundException, InterruptedException, RatReportFailedException {
        PipedReader reader = new PipedReader();
        PipedWriter writer = new PipedWriter(reader);
        ReportTransformer transformer = new ReportTransformer(out, style, reader);
        Thread transformerThread = new Thread(transformer);
        transformerThread.start();
        report(base, writer, matcher, approvedLicenseNames);
        writer.flush();
        writer.close();
        transformerThread.join();
    }
