    public static void main(String[] args) throws Exception {
        String outDir = ".";
        if (args.length < 5) {
            System.out.println("Usage:");
            System.out.println("java -jar Profile.jar <XML-PROFILE> <VEGA|SPEA2|NSGA2> <NumTrials:1..Inf> <NumOfIndividuals:1..Inf> <MaxGenerations:1..Inf> <UniformAnalysis:true|false>");
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
        Integer maxGenerations = Integer.valueOf(args[4]);
        Boolean uniformAnalysis = false;
        if (args.length > 5) uniformAnalysis = Boolean.valueOf(args[5]);
        Profile.initializeProblem(proPath);
        BufferedWriter loggerMetrics = new BufferedWriter(new FileWriter(new File(proPath + "." + algorithmName + ".MetricsAvg")));
        ArrayList<Double> spreads = new ArrayList<Double>();
        ArrayList<Double> spacings = new ArrayList<Double>();
        ArrayList<Double> times = new ArrayList<Double>();
        Moea algorithm = null;
        for (int i = 0; i < numberOfTrials; i++) {
            BufferedWriter loggerPop = new BufferedWriter(new FileWriter(new File(proPath + "." + algorithmName + "." + i)));
            System.out.println("Iteration number: " + i);
            Population<Chromosome> popIni = new Population<Chromosome>();
            for (int k = 0; k < numberOfIndividuals; ++k) {
                Profile ind = new Profile();
                popIni.add(ind);
            }
            if (algorithmName.equals("VEGA")) algorithm = new Vega("Vega", popIni, maxGenerations, 0.80, 0.01); else if (algorithmName.equals("SPEA2")) algorithm = new Spea2("Spea2", popIni, maxGenerations, 0.80, 0.01); else if (algorithmName.equals("NSGA2")) algorithm = new Nsga2("Nsga2", popIni, maxGenerations, 0.80, 0.01);
            double start = System.currentTimeMillis();
            while (!algorithm.done()) {
                if (algorithm.getCurrentGeneration() % 100 == 0) {
                    System.out.println("Current generation: " + algorithm.getCurrentGeneration());
                }
                algorithm.step();
            }
            double end = System.currentTimeMillis();
            spreads.add(algorithm.getPopulation().calculateSpread());
            spacings.add(algorithm.getPopulation().calculateSpacing());
            times.add((end - start) / 1000.0);
            System.out.println("Spread: " + spreads.get(spreads.size() - 1));
            System.out.println("Spacing: " + spacings.get(spacings.size() - 1));
            System.out.println("Time: " + (end - start) / 1000);
            System.out.println("done.");
            loggerPop.write(ParetoFrontToString(algorithm.getPopulation()));
            loggerPop.flush();
            loggerPop.close();
        }
        if (uniformAnalysis) saveUniformObjectives(proPath);
        double spreadMean = calculateMean(spreads);
        double spacingMean = calculateMean(spacings);
        double timeMean = calculateMean(times);
        double spreadStd = calculateStd(spreads, spreadMean);
        double spacingStd = calculateStd(spacings, spacingMean);
        double timeStd = calculateStd(times, timeMean);
        loggerMetrics.write("Spread(Mean,Std):\t" + spreadMean + "\t" + spreadStd + "\n");
        loggerMetrics.write("Spacing(Mean,Std):\t" + spacingMean + "\t" + spacingStd + "\n");
        loggerMetrics.write("Time(Mean,Std):\t" + timeMean + "\t" + timeStd + "\n");
        loggerMetrics.flush();
        loggerMetrics.close();
    }
