    public CommandLine parseOptions(Configuration conf, String[] args) throws ParseException, IOException {
        myconf = conf;
        setDefaultProperties(conf);
        File configFile = getRcFile(args);
        if (configFile != null) loadConfig(conf, configFile);
        CommandLine line = new GenericOptionsParser(conf, options, args).getCommandLine();
        if (line == null) throw new ParseException("Error parsing command line");
        if (line.hasOption(opt_nReduceTasks.getOpt())) {
            String rString = line.getOptionValue(opt_nReduceTasks.getOpt());
            try {
                int r = Integer.parseInt(rString);
                if (r >= minReduceTasks) nReduceTasks = r; else throw new ParseException("Number of reducers must be greater than or equal to " + minReduceTasks + " (got " + rString + ")");
            } catch (NumberFormatException e) {
                throw new ParseException("Invalid number of reduce tasks '" + rString + "'");
            }
        }
        String[] otherArgs = line.getArgs();
        if (otherArgs.length < 2) throw new ParseException("You must provide input and output paths"); else {
            FileSystem fs;
            for (int i = 0; i < otherArgs.length - 1; ++i) {
                Path p = new Path(otherArgs[i]);
                fs = p.getFileSystem(conf);
                p = p.makeQualified(fs);
                FileStatus[] files = fs.globStatus(p);
                if (files != null && files.length > 0) {
                    for (FileStatus status : files) inputs.add(status.getPath());
                } else throw new ParseException("Input path " + p.toString() + " doesn't exist");
            }
            outputDir = new Path(otherArgs[otherArgs.length - 1]);
            fs = outputDir.getFileSystem(conf);
            outputDir = outputDir.makeQualified(fs);
            if (fs.exists(outputDir)) throw new ParseException("Output path " + outputDir.toString() + " already exists.  Won't overwrite");
        }
        return line;
    }
