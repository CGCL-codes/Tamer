    public static void main(String[] args) {
        String annotationFilePath;
        String descriptionFilePath;
        int metric = 4;
        int clusterMethod = 0;
        double bioSignificanceThreshold;
        double graphicalSigThreshold;
        TMEVBridge tmev;
        loadGOinfo goloader;
        runInfo runInfor;
        Vector<ClusterMethod> methods = new Vector<ClusterMethod>();
        Map<String, GOnumber> allGOnum;
        Map<String, String> geneAnnotationHash;
        int BCF = 0;
        dataProcessing dataProcesser;
        progressBarGUI progress;
        methods.add(new KMCClusteringMethod());
        methods.add(new KMSClusteringMethod());
        runInfor = new runInfo(methods);
        Thread runInforThread = new Thread(runInfor);
        runInforThread.setPriority(Thread.NORM_PRIORITY);
        runInforThread.start();
        try {
            runInforThread.join();
        } catch (InterruptedException e) {
            System.err.println("Option frame was interrupted!");
        }
        annotationFilePath = runInfor.getAnnotationFilePath();
        bioSignificanceThreshold = runInfor.getBioSignificanceThreshold();
        graphicalSigThreshold = runInfor.getGraphicalSigThreshold();
        metric = runInfor.getMetric();
        clusterMethod = runInfor.getClusterMethod();
        descriptionFilePath = runInfor.getGODescriptionFile();
        tmev = new TMEVBridge();
        tmev.loadData();
        goloader = new loadGOinfo(annotationFilePath, tmev, descriptionFilePath);
        allGOnum = goloader.getAllGOnum();
        geneAnnotationHash = goloader.getGeneAnnotationHash();
        BCF = goloader.getBCF();
        int finish = 0;
        methods.get(clusterMethod).run(metric, geneAnnotationHash, allGOnum, BCF, tmev.getDataFrame());
        finish = methods.get(clusterMethod).getNumberOfSteps();
        File resultsDir = new File("Results");
        if (resultsDir.isDirectory()) deleteDir(resultsDir);
        resultsDir.mkdir();
        File logDir = new File("Results" + File.separator + "LogFiles");
        logDir.mkdir();
        File clusterDir = new File("Results" + File.separator + "Cluster");
        clusterDir.mkdir();
        dataProcesser = new dataProcessing(bioSignificanceThreshold, graphicalSigThreshold, allGOnum, finish);
        progress = new progressBarGUI(0, (finish * 5) + 10);
        methods.get(clusterMethod).setGenes(tmev.getPopulationList());
        methods.get(clusterMethod).cluster(tmev, progress, dataProcesser);
        progress.update("DONE CLUSTERING");
        progress.update("Printing out Data");
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("Results" + File.separator + "settings.txt"));
            writer.write("Clustering Method: " + methods.get(clusterMethod).getClusterMethodString() + "\n");
            writer.close();
        } catch (IOException e) {
        }
        Vector<String> geneList = new Vector<String>();
        try {
            BufferedReader genereader = new BufferedReader(new FileReader("Results" + File.separator + "genelist.txt"));
            String line;
            while ((line = genereader.readLine()) != null) {
                line = line.trim();
                if (line.length() > 0) {
                    geneList.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        try {
            boolean notFirst;
            BufferedWriter writer = new BufferedWriter(new FileWriter("Results" + File.separator + "geneInputData.txt"));
            BufferedReader reader = new BufferedReader(new FileReader(descriptionFilePath));
            String line;
            StringTokenizer token1, token2;
            writer.write("ontology\tdescription\tcontrolledByFound");
            writer.newLine();
            while ((line = reader.readLine()) != null) {
                if (line.trim().length() > 0) {
                    token1 = new StringTokenizer(line, "\t");
                    String gene = token1.nextToken().trim();
                    if (geneList.contains(gene)) geneList.remove(gene);
                    String des = "description unknown";
                    if (token1.hasMoreTokens()) des = token1.nextToken().trim();
                    writer.write(gene + "\t" + des + "\t");
                    if (geneAnnotationHash.get(gene) != null) {
                        token2 = new StringTokenizer(geneAnnotationHash.get(gene), ";");
                        notFirst = false;
                        while (token2.hasMoreTokens()) {
                            if (notFirst) writer.write("; "); else notFirst = true;
                            writer.write(token2.nextToken().trim());
                        }
                    }
                    writer.newLine();
                }
            }
            for (String gene : geneList) {
                writer.write(gene + "\tdescription unknown\t");
                if (geneAnnotationHash.get(gene) != null) {
                    token2 = new StringTokenizer(geneAnnotationHash.get(gene), ";");
                    notFirst = false;
                    while (token2.hasMoreTokens()) {
                        if (notFirst) writer.write("; "); else notFirst = true;
                        writer.write(token2.nextToken().trim());
                    }
                }
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            System.err.println("Could not create file for input data!");
            e.printStackTrace(System.err);
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(annotationFilePath));
            BufferedWriter writer = new BufferedWriter(new FileWriter("Results" + File.separator + "geneAnnotation.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.write("\n");
            }
            writer.close();
            reader.close();
        } catch (IOException e) {
            System.err.println("Could not copy input file");
        }
        dataProcesser.printGOseries();
        progress.update("Printing GOseries Logs");
        dataProcesser.printBioSigTrend();
        progress.update("Printing Bio Series Trends");
        dataProcesser.flagBioGraphSig();
        progress.update("Printing Graphical Series Trends");
    }
