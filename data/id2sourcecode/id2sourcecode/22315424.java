    static void init(String[] args) {
        parser = new AutoHelpCmdLineParser();
        urlO = parser.addHelp(parser.addStringOption("url"), "Start GenomeView with data loaded from the URL");
        fileO = parser.addHelp(parser.addStringOption("file"), "Start GenomeView with data loaded from a file.");
        configurationO = parser.addHelp(parser.addStringOption("config"), "Provide additional configuration to load.");
        positionO = parser.addHelp(parser.addStringOption("position"), "Provide the initial region that should be visible.");
        sessionO = parser.addHelp(parser.addStringOption("session"), "Provide a session file that contains all the files that have to be loaded.");
        idO = parser.addHelp(parser.addStringOption("id"), "Instance ID for this GenomeView instance, useful to control multiple GVs at once.");
        goodParse = parse(parser, args);
        if (parser.checkHelp()) {
            System.exit(0);
        }
        String config = (String) parser.getOptionValue(configurationO);
        if (config != null) {
            try {
                if (config.startsWith("http") || config.startsWith("ftp")) {
                    Configuration.loadExtra(URIFactory.url(config).openStream());
                } else {
                    Configuration.loadExtra(new FileInputStream(config));
                }
            } catch (FileNotFoundException e) {
                logger.log(Level.SEVERE, "loading extra configuration", e);
            } catch (MalformedURLException e) {
                logger.log(Level.SEVERE, "loading extra configuration", e);
            } catch (IOException e) {
                logger.log(Level.SEVERE, "loading extra configuration", e);
            } catch (URISyntaxException e) {
                logger.log(Level.SEVERE, "loading extra configuration", e);
            }
        }
    }
