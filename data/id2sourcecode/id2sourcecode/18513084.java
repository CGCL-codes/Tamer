    public static void encode(BaseNanoList list, URL style, OutputStream out) throws IOException, SAXException {
        XSLProcessorImpl processor = ProcessorFactory.createProcessor(style, out);
        EncoderThread writer = new EncoderThread(list);
        try {
            writer.start();
            processor.parse(writer.getInputSource());
            writer.join();
        } catch (InterruptedException ex) {
        }
    }
