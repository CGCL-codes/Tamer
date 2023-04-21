    public static void main(String[] args) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Date date = new java.sql.Date(System.currentTimeMillis());
        String timeStamp = simpleDateFormat.format(date);
        String usageInfo = "Options";
        Options options = new Options();
        options.addOption("f", true, "File location of the file(s) to be plotted or compared");
        options.addOption("v", true, "Version information of the file(s) to be plotted or compared");
        options.addOption("o", true, "Name of pdf file where to generate output");
        options.addOption("w", true, "Optional Image Width");
        options.addOption("h", true, "Optional Image Height");
        options.addOption("r", false, "Override output file if it exists");
        options.addOption("i", false, "Optional create images");
        options.addOption("idir", true, "Optional directory where to create images");
        options.addOption("d", false, "Optional create data html file");
        options.addOption("dout", true, "Optional html file name where to create data output");
        CommandLine cmd = null;
        CommandLineParser parser = new PosixParser();
        try {
            cmd = parser.parse(options, args);
        } catch (Exception e) {
            System.err.println("Error parsing arguments");
            e.printStackTrace();
            System.exit(1);
        }
        if (!cmd.hasOption("f")) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(usageInfo, options);
            System.exit(1);
        }
        String fls = cmd.getOptionValue("f");
        String[] files = fls.split(",");
        if (files.length > 1) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("Currently processing of multiple input files is not supported\n" + usageInfo, options);
            System.exit(1);
        }
        String[] versions = null;
        if (cmd.hasOption("v")) {
            String ver = cmd.getOptionValue("v");
            versions = ver.split(",");
            if (versions.length != files.length) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("Number of versions provided should be same as number of file to be processed\n" + usageInfo, options);
                System.exit(1);
            }
        } else {
            versions = new String[files.length];
            for (int i = 0; i < versions.length; i++) {
                versions[i] = "Version " + (i + 1);
            }
        }
        int imgWidth = -1;
        int imgHeight = -1;
        if (cmd.hasOption("w")) {
            imgWidth = Integer.parseInt(cmd.getOptionValue("w").trim());
        }
        if (cmd.hasOption("h")) {
            imgHeight = Integer.parseInt(cmd.getOptionValue("h").trim());
        }
        boolean override = false;
        if (cmd.hasOption("r")) {
            override = true;
        }
        String outFile = null;
        String outFileName = null;
        if (cmd.hasOption("o")) {
            outFile = cmd.getOptionValue("o");
        } else {
            outFile = "zoie_index_data.pdf";
        }
        File existFile = new File(outFile);
        outFileName = existFile.getAbsolutePath();
        if (existFile.exists()) {
            if (!override) {
                String dir = existFile.getAbsoluteFile().getParent();
                String fname = existFile.getName();
                if (fname.indexOf(".") != -1) {
                    fname = fname.substring(0, fname.lastIndexOf(".")) + "_" + timeStamp + fname.substring(fname.lastIndexOf("."));
                }
                outFileName = dir + File.separator + fname;
            }
        }
        if (!outFileName.endsWith(".pdf")) {
            outFileName = outFileName + ".pdf";
        }
        String propFileName = null;
        if (cmd.hasOption("env")) {
            propFileName = cmd.getOptionValue("env");
        }
        boolean createPngs = cmd.hasOption("i");
        String pngDir = null;
        if (cmd.hasOption("idir")) {
            pngDir = cmd.getOptionValue("idir");
        }
        boolean createHtml = false;
        if (cmd.hasOption("d")) {
            createHtml = true;
        }
        String htmlOutFileName = null;
        if (createHtml) {
            String htmlOutFile = null;
            if (cmd.hasOption("dout")) {
                htmlOutFile = cmd.getOptionValue("dout");
            } else {
                htmlOutFile = "zoie_index_html_data.html";
            }
            File existFile1 = new File(htmlOutFile);
            htmlOutFileName = existFile1.getAbsolutePath();
            if (existFile1.exists()) {
                if (!override) {
                    String dir = existFile1.getAbsoluteFile().getParent();
                    String fname = existFile1.getName();
                    if (fname.indexOf(".") != -1) {
                        fname = fname.substring(0, fname.lastIndexOf(".")) + "_" + timeStamp + fname.substring(fname.lastIndexOf("."));
                    }
                    htmlOutFileName = dir + File.separator + fname;
                }
            }
            if (!htmlOutFileName.endsWith(".html")) {
                htmlOutFileName = htmlOutFileName + ".html";
            }
        }
        createHtml = true;
        Map<Integer, String> indSortMap = null;
        try {
            ZoieIndexLogProcessor.plot(new ZoieIndexLogProcessor(files[0]), outFileName, versions[0], imgWidth, imgHeight, createPngs, pngDir, createHtml, htmlOutFileName);
            System.out.println("Ouput File Name: " + outFileName);
        } catch (Exception e1) {
            System.out.println("Exception in processing ....");
            e1.printStackTrace();
            System.exit(1);
        }
    }
