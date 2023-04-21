    public static void main(String[] args) {
        if (args.length < 3) {
            printUsage();
            return;
        }
        try {
            int gridSize = Integer.parseInt(args[2]);
            double resolution = Double.parseDouble(args[4]);
            int maxThreads = Integer.parseInt(args[5]);
            String[] sbbox = args[3].split(",");
            double[] bbox = new double[4];
            bbox[0] = Double.parseDouble(sbbox[0]);
            bbox[1] = Double.parseDouble(sbbox[1]);
            bbox[2] = Double.parseDouble(sbbox[2]);
            bbox[3] = Double.parseDouble(sbbox[3]);
            String recordsFile = args[1] + "_records.csv";
            Records records = null;
            if (new File(recordsFile).exists()) {
                records = new Records(recordsFile);
            } else {
                try {
                    records = new Records(args[0], "*:*", bbox, recordsFile);
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        if (args.length > 1 && new File(args[1] + "_records.csv").exists()) {
                            new File(args[1] + "_records.csv").delete();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                records = null;
                records = new Records(recordsFile);
            }
            records.removeSpeciesNames();
            System.gc();
            OccurrenceDensity occurrenceDensity = new OccurrenceDensity(gridSize, resolution, bbox);
            occurrenceDensity.write(records, args[1], null, maxThreads, true, true);
            System.gc();
            SpeciesDensity speciesDensity = new SpeciesDensity(gridSize, resolution, bbox);
            speciesDensity.write(records, args[1], null, maxThreads, true, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        printUsage();
    }
