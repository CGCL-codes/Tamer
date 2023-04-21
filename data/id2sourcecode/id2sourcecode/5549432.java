    public static void main(final String[] args) {
        Id fromNodeId;
        Id toNodeId;
        log.info("starting RouterVis demo");
        final String testConfigFile = "./examples/siouxfalls/config.xml";
        Class<? extends VisLeastCostPathCalculator> router = VisDijkstra.class;
        String outputDirSuffix = "/DijkstraRouter/";
        if (args.length == 4) {
            if (args[3].equals("PSLogitRouter")) {
                router = PSLogitRouter.class;
                outputDirSuffix = "VisDijkstra/";
            } else if (args[3].equals("CLogitRouter")) {
                router = CLogitRouter.class;
            } else if (args[3].equals("DijkstraRouter")) {
                router = VisDijkstra.class;
            } else {
                throw new RuntimeException("No such router: " + args[3] + "!");
            }
            outputDirSuffix = "/" + args[3];
        }
        Config config = null;
        if (args.length >= 3) {
            config = Gbl.createConfig(new String[] { args[0], "config_v1.dtd" });
            fromNodeId = new IdImpl(args[1]);
            toNodeId = new IdImpl(args[2]);
        } else {
            log.info(" reading default config file: " + testConfigFile);
            config = Gbl.createConfig(new String[] { testConfigFile });
            fromNodeId = new IdImpl("13");
            toNodeId = new IdImpl("7");
        }
        log.info(" done.");
        config.controler().setOutputDirectory(config.controler().getOutputDirectory() + outputDirSuffix);
        log.info("  reading the network...");
        NetworkLayer network = null;
        network = new NetworkLayer();
        new MatsimNetworkReader(network).readFile(config.network().getInputFile());
        log.info("  done.");
        log.info("  creating output dir if needed");
        final File outputDir = new File(config.controler().getOutputDirectory());
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        } else if (outputDir.list().length > 0) {
            log.error("The output directory " + outputDir + " exists already but has files in it! Please delete its content or the directory and start again. We will not delete or overwrite any existing files.");
            System.exit(-1);
        }
        log.info("done");
        log.info("  creating RouterVis object.");
        final TravelTime costCalc = new FreespeedTravelTimeCost();
        final RouterVis vis = new RouterVis(network, (TravelCost) costCalc, costCalc, router);
        log.info("  done.");
        log.info("  running RouterVis.");
        final Node fromNode = network.getNode(fromNodeId.toString());
        final Node toNode = network.getNode(toNodeId.toString());
        vis.runRouter(fromNode, toNode, 0.0);
        log.info("  done.");
        log.info("  starting NetVis.");
        final String[] visargs = { config.controler().getOutputDirectory() + "/Snapshot" };
        Gbl.reset();
        NetVis.main(visargs);
        log.info("  done.");
    }
