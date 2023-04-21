    public static void main(String[] args) {
        RbnbToSrb serb = new RbnbToSrb();
        Options opts = new Options();
        CommandLineParser parser = new BasicParser();
        CommandLine cmd = null;
        opts.addOption("a", false, "about");
        opts.addOption("e", false, "delete the filepath in the srb");
        opts.addOption("h", false, "print usage");
        opts.addOption("s", true, "rbnb server for which to be a data sink");
        HelpFormatter formatter = new HelpFormatter();
        try {
            cmd = parser.parse(opts, args);
        } catch (ParseException pe) {
            logger.severe("Trouble parsing command line: " + pe);
            System.exit(0);
        }
        if (cmd.hasOption("a")) {
            System.out.println("About: this program accepts an rbnb ChannelMap " + "and then forwards the data and metadata to SRB");
            System.exit(0);
        }
        if (cmd.hasOption("e")) {
            serb.clearSrb = true;
        }
        if (cmd.hasOption("h")) {
            formatter.printHelp("RbnbToSrb", opts);
            System.exit(0);
        }
        if (cmd.hasOption("s")) {
            String a = cmd.getOptionValue("s");
            serb.rbnbServer = a;
        }
        try {
            if (serb.writeCmapToSrb(serb.getChannelMap())) {
                logger.info("Wrote to SRB.");
            }
        } catch (SAPIException sae) {
            logger.severe("Cannot get a channelmap: " + sae);
            sae.printStackTrace();
        } catch (IOException ioe) {
            logger.severe("Writing cmap to srb: " + ioe);
        }
    }
