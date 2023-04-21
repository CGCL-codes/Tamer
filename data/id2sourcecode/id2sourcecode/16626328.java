    public static void main(String[] args) {
        File logFile = DEFAULT_LOG_FILE;
        File imageFile = DEFAULT_IMAGE_FILE;
        int imageWidth = DEFAULT_IMAGE_WIDTH;
        int imageHeight = DEFAULT_IMAGE_HEIGHT;
        String imageTitle = DEFAULT_IMAGE_TITLE;
        boolean force = DEFAULT_FORCE;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-h") || args[i].equals("--help")) {
                printUsageAndExit(false);
            } else if (args[i].equals("-i")) {
                if (i + 1 >= args.length) {
                    printUsageAndExit("The '-i' option is missing a filename.");
                }
                logFile = new File(args[++i]);
            } else if (args[i].equals("-o")) {
                if (i + 1 >= args.length) {
                    printUsageAndExit("The '-o' option is missing a filename.");
                }
                imageFile = new File(args[++i]);
            } else if (args[i].equals("-w")) {
                if (i + 1 >= args.length) {
                    printUsageAndExit("The '-w' option is missing the number of pixels.");
                }
                try {
                    imageWidth = Integer.parseInt(args[++i]);
                } catch (NumberFormatException e) {
                    printUsageAndExit("Cannot parse width.");
                }
            } else if (args[i].equals("-h")) {
                if (i + 1 >= args.length) {
                    printUsageAndExit("The '-h' option is missing the number of pixels.");
                }
                try {
                    imageHeight = Integer.parseInt(args[++i]);
                } catch (NumberFormatException e) {
                    System.err.println();
                    printUsageAndExit("Cannot parse height.");
                }
            } else if (args[i].equals("-t")) {
                if (i + 1 >= args.length) {
                    printUsageAndExit("The '-t' option is missing the title string.");
                }
                imageTitle = args[++i];
            } else if (args[i].equals("-f")) {
                force = true;
            } else {
                printUsageAndExit("Unknown argument: " + args[i]);
            }
        }
        if (!logFile.isFile()) {
            System.err.println("The logfile '" + logFile.getPath() + "' does not exist.");
            System.exit(1);
        }
        String imageFilename = imageFile.getName().toLowerCase();
        if (!imageFilename.endsWith(".png") && !imageFilename.endsWith(".jpg") && !imageFilename.endsWith(".jpeg")) {
            System.err.println("The output image filename has to end with '.png', '.jpg' or '.jpeg'");
            System.exit(1);
        }
        if (imageFile.exists() && force == false) {
            System.err.println("The output image file already exist. Set the force (-f) option to overwrite.");
            System.exit(1);
        }
        if (imageWidth <= 0) {
            System.err.println("Width must be > 0");
        }
        if (imageHeight < 0) {
            System.err.println("Height must be > 0");
        }
        new AuthLog2Gantt(logFile, imageFile, force, imageWidth, imageHeight, imageTitle);
    }
