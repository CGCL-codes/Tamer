    public static void main(String[] args) {
        String setting = null;
        String handlerId = null;
        String url = null;
        String outFilename = null;
        System.out.println("Http2Rss loading...");
        if (args.length < 3 || args.length > 4) {
            System.err.println("Usage: Http2Rss [setting] <handlerId> <url> <outfile>");
            System.err.println("\tconfig: (optional) Config file name");
            System.err.println("\tsiteid: One site ID that defined in config file");
            System.err.println("\turl: HTTP URL to fetch");
            System.err.println("\toutfile: RSS filename to write");
            System.err.println("e.g.: http2rss.exe http2rss.properties gnews \"http://news.google.com.hk/news?ned=us&topic=t\" gnews.xml");
            System.exit(1);
        } else if (args.length == 3) {
            setting = "http2rss.properties";
            handlerId = args[0];
            url = args[1];
            outFilename = args[2];
            System.out.println("Config file not specified, searching default http2rss.properties");
        } else {
            setting = args[0];
            handlerId = args[1];
            url = args[2];
            outFilename = args[3];
            System.out.println("Using config file " + setting);
        }
        ContentProcessor proc = null;
        try {
            proc = new ContentProcessor(setting);
        } catch (ConfigurationException ce) {
            System.err.println("Error loading configuration: " + ce.getCause().getMessage());
            System.exit(4);
        }
        ChannelIF channel = null;
        HTTPLoader loader = new HTTPLoader();
        MultiPatternContentHandler handler = new MultiPatternContentHandler();
        try {
            handler.setChannelBuilder(new ChannelBuilder());
            proc.setContentUrl(new URL(url));
            System.out.println("Loading url: " + url + " using configuration " + handlerId);
            proc.setLoader(loader);
            proc.setHandler(handler);
            handler.setHandler(handlerId);
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
        String filename = outFilename;
        RSS_2_0_Exporter exporter = null;
        if (filename == null) {
            System.err.println("No output file specified, please specify it with target.output.file ");
            System.exit(5);
        } else {
            File rssFile = new File(filename);
            String encoding = proc.getConfig().getString("encoding");
            try {
                exporter = new RSS_2_0_Exporter(rssFile, encoding);
            } catch (IOException ioe) {
                System.err.println("Error writing output file: " + filename + ", " + ioe.getMessage());
                System.exit(3);
            }
        }
        try {
            exporter.write(channel);
            System.out.println("RSS " + filename + " wrote successfully!");
        } catch (IOException ie) {
            System.err.println("Error writing output: " + ie.getMessage());
            System.exit(5);
        }
    }
