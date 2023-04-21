    private static GraphStatsContainer dumpGraphStatistics(Graph g, GraphStatsContainer stats, BufferedWriter meanWriter, String outDir, int wave, boolean extendedAnalysis) throws IOException {
        DegreeHistogramThread degreeThread = null;
        ClusteringThread clusteringThread = null;
        CoverageThread coverageThread = new CoverageThread(g, wave);
        AssortativityThread assortativityThread = new AssortativityThread(g);
        ComponentsThread componentsThread = new ComponentsThread(g);
        BetweenessThread betweenessThread = null;
        APLThread aplThread = null;
        if (wave == 0) {
            degreeThread = new DegreeHistogramThread(g, wave, -1, -1);
            clusteringThread = new ClusteringThread(g, wave, -1, -1);
            betweenessThread = new BetweenessThread(g, wave, -1, -1);
            aplThread = new APLThread(g, wave, -1, -1);
        } else {
            degreeThread = new DegreeHistogramThread(g, wave, (int) stats.degreeHistogram.xAxis().lowerEdge(), (int) stats.degreeHistogram.xAxis().upperEdge());
            clusteringThread = new ClusteringThread(g, wave, stats.clusteringHistogram.xAxis().lowerEdge(), stats.clusteringHistogram.xAxis().upperEdge());
            betweenessThread = new BetweenessThread(g, wave, stats.betweenessHistogram.xAxis().lowerEdge(), stats.betweenessHistogram.xAxis().upperEdge());
            aplThread = new APLThread(g, wave, stats.aplHistogram.xAxis().lowerEdge(), stats.aplHistogram.xAxis().upperEdge());
        }
        List<Thread> threads = new ArrayList<Thread>();
        threads.add(coverageThread);
        threads.add(degreeThread);
        threads.add(clusteringThread);
        threads.add(assortativityThread);
        threads.add(componentsThread);
        if (extendedAnalysis) {
            threads.add(betweenessThread);
            threads.add(aplThread);
        }
        for (Thread t : threads) t.start();
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        meanWriter.write(String.valueOf(wave));
        meanWriter.write("\t");
        meanWriter.write(String.valueOf(coverageThread.numVertices));
        meanWriter.write("\t");
        meanWriter.write(String.valueOf(coverageThread.numEdges));
        meanWriter.write("\t");
        meanWriter.write(String.valueOf(coverageThread.numVertices / (float) stats.g.numVertices()));
        meanWriter.write("\t");
        meanWriter.write(String.valueOf(coverageThread.numEdges / (float) stats.g.numEdges()));
        meanWriter.write("\t");
        meanWriter.write(String.valueOf(coverageThread.numVerticesNotTail));
        meanWriter.write("\t");
        meanWriter.write(String.valueOf(coverageThread.numVerticesNotTail / (float) stats.g.numVertices()));
        meanWriter.write("\t");
        meanWriter.write(String.valueOf(coverageThread.numIsolatedVertices));
        meanWriter.write("\t");
        meanWriter.write(String.valueOf((float) degreeThread.getHistogram().mean()));
        meanWriter.write("\t");
        meanWriter.write(String.valueOf((float) clusteringThread.getHistogram().mean()));
        meanWriter.write("\t");
        meanWriter.write(String.valueOf((float) assortativityThread.getAssortativity()));
        meanWriter.write("\t");
        meanWriter.write(String.valueOf(componentsThread.getNumComponents()));
        if (extendedAnalysis) {
            meanWriter.write("\t");
            meanWriter.write(String.valueOf((float) betweenessThread.getHistogram().mean()));
            meanWriter.write("\t");
            meanWriter.write(String.valueOf((float) aplThread.getHistogram().mean()));
        } else {
            meanWriter.write("\tn.a.\tn.a.");
        }
        meanWriter.newLine();
        meanWriter.flush();
        GraphStatistics.writeHistogram(degreeThread.getHistogram(), outDir + DEGREE_DIR + wave + ".degree.png");
        GraphStatistics.writeHistogram(clusteringThread.getHistogram(), outDir + CLUSTERING_DIR + wave + ".clustering.png");
        if (extendedAnalysis) {
            GraphStatistics.writeHistogram(betweenessThread.getHistogram(), outDir + BETWEENESS_DIR + wave + ".betweeness.png");
            GraphStatistics.writeHistogram(aplThread.getHistogram(), outDir + APL_DIR + wave + ".apl.png");
        }
        PajekVisWriter pajekWriter = new PajekVisWriter();
        pajekWriter.write(g, outDir + PAJEK_DIR + wave + ".sampled.net");
        GraphStatsContainer container = new GraphStatsContainer();
        container.g = g;
        container.degreeHistogram = degreeThread.getHistogram();
        container.clusteringHistogram = clusteringThread.getHistogram();
        container.betweenessHistogram = betweenessThread.getHistogram();
        container.aplHistogram = aplThread.getHistogram();
        return container;
    }
