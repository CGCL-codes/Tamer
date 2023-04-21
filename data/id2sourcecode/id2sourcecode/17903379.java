    @SuppressWarnings("static-access")
    public static final void main(String[] args) throws Exception {
        MainExecutive exec = new MainExecutive();
        Options options = new Options();
        options.addOption("h", false, "Show this help.");
        Option channelName = OptionBuilder.withArgName("name").hasArg().withDescription("connect to the channel name").create("n");
        options.addOption(channelName);
        Option slots = OptionBuilder.withArgName("int").hasArg().withDescription("the number or executive slots to provide").create("s");
        options.addOption(slots);
        Option maxSize = OptionBuilder.withArgName("int").hasArg().withDescription("the maximum cluster size to operate in").create("m");
        options.addOption(slots);
        Option props = OptionBuilder.withArgName("props").hasArg().withDescription("the channel props string").create("p");
        options.addOption(props);
        Option url = OptionBuilder.withArgName("url").hasArg().withDescription("the channel props url").create("u");
        options.addOption(url);
        CommandLineParser parser = new PosixParser();
        CommandLine cmd = parser.parse(options, args);
        if (cmd.hasOption('h')) {
            usage(options, 0);
        }
        if (cmd.hasOption('n')) {
            exec.getExecutive().setClusterName(cmd.getOptionValue('n'));
        }
        if (cmd.hasOption('s')) {
            exec.getExecutive().setSlotCount(Integer.parseInt(cmd.getOptionValue('s')));
        }
        if (cmd.hasOption('p')) {
            exec.getExecutive().setChannelStringProps(cmd.getOptionValue('p'));
        }
        if (cmd.hasOption('m')) {
            exec.getExecutive().setMaxGroupSize(Integer.parseInt(cmd.getOptionValue('m')));
        }
        if (cmd.hasOption('u')) {
            exec.getExecutive().setChannelUrlProps(new URL(cmd.getOptionValue('u')));
        }
        log.info("Using slots:" + exec.getExecutive().getSlotCount());
        log.info("Using max group size:" + exec.getExecutive().getMaxGroupSize());
        log.info("Using channel name:" + exec.getExecutive().getClusterName());
        log.info("Using channel props:" + exec.getExecutive().getChannelStringProps());
        log.info("Using channel URL props:" + exec.getExecutive().getChannelUrlProps());
        exec.run();
    }
