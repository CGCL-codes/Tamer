    public static void main(String[] args) throws Exception {
        String outDir = ".";
        if (args.length < 5) {
            System.out.println("Usage:");
            System.out.println("java -jar Analysis.jar <BASENAME> <NumObjectives:1..Inf> <NumTrials:1..Inf>");
            args = new String[6];
            args[0] = "Physics.pro";
            args[1] = "VEGA";
            args[2] = "0";
            args[3] = "200";
            args[4] = "4000";
            args[5] = "true";
            outDir = "D:/jlrisco/Trabajo/MisPapers/IEEE T EVOLUT COMPUT/2007/Results";
            return;
        }
        String proPath = outDir + File.separator + args[0];
        String algorithmName = args[1];
        Integer numberOfTrials = Integer.valueOf(args[2]);
        Integer numberOfIndividuals = Integer.valueOf(args[3]);
        BufferedWriter loggerMetrics = new BufferedWriter(new FileWriter(new File(proPath + "." + algorithmName + ".MetricsAvg")));
        ArrayList<Double> spreads = new ArrayList<Double>();
        ArrayList<Double> spacings = new ArrayList<Double>();
        for (int i = 0; i < numberOfTrials; i++) {
            System.out.println("Checking iteration number: " + i);
            Population<Chromosome> popIni = new Population<Chromosome>();
            for (int k = 0; k < numberOfIndividuals; ++k) {
                Analysis ind = new Analysis();
                popIni.add(ind);
            }
            spreads.add(popIni.calculateSpread());
            spacings.add(popIni.calculateSpacing());
            System.out.println("Spread: " + spreads.get(spreads.size() - 1));
            System.out.println("Spacing: " + spacings.get(spacings.size() - 1));
            System.out.println("done.");
        }
        double spreadMean = calculateMean(spreads);
        double spacingMean = calculateMean(spacings);
        double spreadStd = calculateStd(spreads, spreadMean);
        double spacingStd = calculateStd(spacings, spacingMean);
        loggerMetrics.write("Spread(Mean,Std):\t" + spreadMean + "\t" + spreadStd + "\n");
        loggerMetrics.write("Spacing(Mean,Std):\t" + spacingMean + "\t" + spacingStd + "\n");
        loggerMetrics.flush();
        loggerMetrics.close();
    }
