    public static void main(String[] args) {
        ContentProcessor proc = null;
        try {
            proc = new ContentProcessor();
        } catch (ConfigurationException ce) {
            System.err.println("Error loading configuration: " + ce.getMessage());
            System.exit(4);
        }
        ChannelIF channel = null;
        ContentLoaderIF loader = null;
        ContentHandlerIF handler = null;
        try {
            loader = (ContentLoaderIF) Class.forName(proc.getConfig().getString("loader.class")).newInstance();
        } catch (Exception e) {
            System.err.println("Cannot finding ContentLoader: " + proc.getConfig().getString("loader.class"));
            System.exit(6);
        }
        try {
            handler = (ContentHandlerIF) Class.forName(proc.getConfig().getString("handler.class")).newInstance();
            handler.setChannelBuilder(new ChannelBuilder());
        } catch (Exception e) {
            System.err.println("Cannot finding ContentHandler: " + proc.getConfig().getString("handler.class"));
            System.exit(6);
        }
        try {
            proc.setContentUrl(new URL(proc.getConfig().getString("target.input.url")));
            proc.setLoader(loader);
            proc.setHandler(handler);
        } catch (ConfigurationException ce) {
            System.err.println("Error loading configuration: " + ce.getMessage());
            System.exit(4);
        } catch (MalformedURLException me) {
            System.err.println("URL not specified in target.input.url, or the URL is invalid: " + proc.getConfig().getString("target.input.url") + ", " + me.getMessage());
            System.exit(4);
        }
        try {
            proc.process();
            channel = proc.getChannel();
        } catch (LoaderException le) {
            System.err.println("Error loading resources: " + le.getCause().getMessage());
            System.exit(1);
        } catch (ContentHandlerException che) {
            System.err.println("Error handling content: " + che.getMessage());
            System.exit(2);
        } catch (ContentProcessorException cpe) {
            System.err.println("Error occured while converting content: " + cpe.getCause().getMessage());
            System.exit(3);
        }
        String filename = proc.getConfig().getString("target.output.file", "http2rss.xml");
        RSS_1_0_Exporter exporter = null;
        if (filename == null) {
            System.err.println("No output file specified, please specify it with target.output.file ");
            System.exit(5);
        } else {
            File rssFile = new File(filename);
            String encoding = proc.getConfig().getString("encoding");
            try {
                exporter = new RSS_1_0_Exporter(rssFile, encoding);
            } catch (IOException ioe) {
                System.err.println("Error writing output file: " + filename + ", " + ioe.getMessage());
                System.exit(3);
            }
        }
        try {
            exporter.write(channel);
        } catch (IOException ie) {
            System.err.println("Error writing output: " + ie.getMessage());
            System.exit(5);
        }
    }
