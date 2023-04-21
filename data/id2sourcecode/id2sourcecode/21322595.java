    public static void main(String[] args) throws Exception {
        String outDir = ".";
        if (args.length < 5) {
            System.out.println("Usage:");
            System.out.println("java -jar ManualThreadsEmulator.jar <XML-PROFILE> <NumTrials:1..Inf> <MaxGenerations:1..Inf> <NSGA2|SPEA2> <NSGA2|SPEA2> ... <NSGA2|SPEA2> <TotalNumOfIndividuals>");
            args = new String[6];
            args[0] = "Physics.pro";
            args[1] = "1";
            args[2] = "4000";
            args[3] = "NSGA2";
            args[4] = "100";
            outDir = "D:/jlrisco/Trabajo/MisPapers/IEEE T EVOLUT COMPUT/2007/Results";
            return;
        }
        String proPath = outDir + File.separator + args[0];
        Integer numberOfTrials = Integer.valueOf(args[1]);
        Integer maxGenerations = Integer.valueOf(args[2]);
        Integer popSize = Integer.valueOf(args[args.length - 1]);
        Integer numThreads = args.length - 4;
        Integer subPopSize = Profile.getSubPopSize(popSize, numThreads);
        Integer migrationRate = Profile.getMigrationRate(popSize);
        String algorithmName = "";
        for (int i = 0; i < numThreads; ++i) algorithmName += args[3 + i].charAt(0);
        Profile.initializeProblem(proPath);
        BufferedWriter loggerMetrics = new BufferedWriter(new FileWriter(new File(proPath + "." + algorithmName + ".MetricsAvg")));
        ArrayList<Double> spreads = new ArrayList<Double>();
        ArrayList<Double> spacings = new ArrayList<Double>();
        ArrayList<Double> times = new ArrayList<Double>();
        ArrayList<Double> nds = new ArrayList<Double>();
        for (int i = 0; i < numberOfTrials; i++) {
            BufferedWriter loggerPop = new BufferedWriter(new FileWriter(new File(proPath + "." + algorithmName + "." + i)));
            System.out.println("Iteration number: " + i);
            ArrayList<Moea> islands = new ArrayList<Moea>();
            for (int j = 0; j < numThreads; ++j) {
                String subAlgName = String.valueOf(args[3 + j]);
                Population<Chromosome> popIni = new Population<Chromosome>();
                for (int k = 0; k < subPopSize; ++k) {
                    ManualThreadsEmulator individual = new ManualThreadsEmulator();
                    popIni.add(individual);
                }
                Moea island = null;
                if (subAlgName.equals("NSGA2")) island = new Nsga2("Island" + j, popIni, maxGenerations, 0.80, 0.01, migrationRate); else if (subAlgName.equals("SPEA2")) island = new Spea2("Island" + j, popIni, maxGenerations, 0.80, 0.01, migrationRate);
                islands.add(island);
            }
            double start = System.currentTimeMillis();
            double incrI = 0.0, incrF = 0.0, incrT = 0.0;
            int currentGeneration = 0;
            while (currentGeneration < maxGenerations) {
                incrI = System.currentTimeMillis();
                for (int j = 0; j < numThreads - 1; ++j) islands.get(j).step();
                incrF = System.currentTimeMillis();
                incrT += (incrF - incrI);
                islands.get(numThreads - 1).step();
                currentGeneration++;
                if (currentGeneration % 100 == 0) {
                    incrI = System.currentTimeMillis();
                    Moea islandFrom, islandTo;
                    for (int j = 1; j < numThreads; ++j) {
                        islandFrom = islands.get(j - 1);
                        islandTo = islands.get(j);
                        Population<Chromosome> popToSend = islandFrom.filter(islandFrom.getPopulation(), migrationRate);
                        islandTo.receive(popToSend);
                    }
                    islandFrom = islands.get(numThreads - 1);
                    islandTo = islands.get(0);
                    incrF = System.currentTimeMillis();
                    incrT += (incrF - incrI);
                    Population<Chromosome> popToSend = islandFrom.filter(islandFrom.getPopulation(), migrationRate);
                    islandTo.receive(popToSend);
                }
            }
            double end = System.currentTimeMillis();
            Population<Chromosome> pop = new Population<Chromosome>();
            for (Moea island : islands) pop.add(island.getPopulation());
            pop.keepNonDominated();
            spreads.add(pop.calculateSpread());
            spacings.add(pop.calculateSpacing());
            nds.add(1.0 * pop.size());
            times.add((end - start - incrT) / 1000.0);
            System.out.println("Spread: " + spreads.get(spreads.size() - 1));
            System.out.println("Spacing: " + spacings.get(spacings.size() - 1));
            System.out.println("NDs: " + nds.get(nds.size() - 1));
            System.out.println("Time: " + (end - start) / 1000);
            System.out.println("done.");
            loggerPop.write(PopulationToString(pop));
            loggerPop.flush();
            loggerPop.close();
        }
        double spreadMean = calculateMean(spreads);
        double spreadStd = calculateStd(spreads, spreadMean);
        double spacingMean = calculateMean(spacings);
        double spacingStd = calculateStd(spacings, spacingMean);
        double ndsMean = calculateMean(nds);
        double ndsStd = calculateStd(nds, ndsMean);
        double timeMean = calculateMean(times);
        double timeStd = calculateStd(times, timeMean);
        loggerMetrics.write("Spread(Mean,Std):\t" + spreadMean + "\t" + spreadStd + "\n");
        loggerMetrics.write("Spacing(Mean,Std):\t" + spacingMean + "\t" + spacingStd + "\n");
        loggerMetrics.write("NDs(Mean,Std):\t" + ndsMean + "\t" + ndsStd + "\n");
        loggerMetrics.write("Time(Mean,Std):\t" + timeMean + "\t" + timeStd + "\n");
        loggerMetrics.flush();
        loggerMetrics.close();
    }
