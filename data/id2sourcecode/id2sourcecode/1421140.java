    private static void generate() throws Exception {
        System.out.print("This will erase all existing test output, continue? [y/n]: ");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String userConfirm = null;
        try {
            userConfirm = br.readLine();
        } catch (IOException ioe) {
            System.out.println("IO error reading input");
            System.exit(1);
        }
        if (!userConfirm.equalsIgnoreCase("y")) {
            System.out.println("exiting");
            System.exit(0);
        }
        File inputDir = new File(TEST_INPUT);
        File outputDir = new File(TEST_OUTPUT);
        for (File output : outputDir.listFiles()) {
            if (!output.getPath().endsWith(".xml")) {
                continue;
            }
            System.out.println("deleting " + output.getPath());
            output.delete();
        }
        Fits fits = new Fits("");
        for (File input : inputDir.listFiles()) {
            if (input.isDirectory()) {
                continue;
            }
            System.out.println("processing " + input.getPath());
            FitsOutput fitsOutput = fits.examine(input);
            fitsOutput.saveToDisk(outputDir.getPath() + File.separator + input.getName() + ".xml");
        }
        System.out.println("All Done");
    }
