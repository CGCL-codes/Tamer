    private ParseTreeSerializerBenchmark(String file, int numRuns) throws Exception {
        File inputFile = new File(file);
        if (!inputFile.exists() || !inputFile.canRead()) {
            System.err.println("Input file: " + file + " not found or can't be read.");
            System.exit(1);
        }
        content = new String(IOUtils.toByteArray(new FileInputStream(file)));
        this.numRuns = 10;
        warmup = true;
        runNekoSimple();
        Thread.sleep(10000L);
        this.numRuns = numRuns;
        warmup = false;
        runNekoSimple();
    }
