    public static void main(String[] args) throws FileNotFoundException, IOException {
        args = new String[] { "-r2", "-s 2", "-e=0", "-a5000", "-f1.0", "-m qsim" };
        double flowFactor = 1.0;
        int nOfThreadsReplanning = 4;
        int nOfThreadsSim = 3;
        int nOfThreadsEvents = 1;
        int nOfAgents = 10000;
        String mobsim = "qsim";
        String outputDir = "output_";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        outputDir += sdf.format(cal.getTime());
        Iterator<String> iter = new ArgumentParser(args, false).iterator();
        while (iter.hasNext()) {
            String arg = iter.next();
            if (arg.startsWith("-h")) {
                System.out.println("MATSim Benchmark v2");
                System.out.println("Arguments: ");
                System.out.println("  -r n    Number of threads for replanning [default: " + nOfThreadsReplanning + "]");
                System.out.println("  -s n    Number of threads for simulation [default: " + nOfThreadsSim + "]");
                System.out.println("  -e n    Number of threads for events handling [default: " + nOfThreadsEvents + "]");
                System.out.println("  -a n    Number of agents to generate [default: " + nOfAgents + "]");
                System.out.println("  -f n    Flow capacity factor to use [default: " + flowFactor + "]");
                System.out.println("  -o dir  output-directory to use [default: output_DATE-TIME]");
                System.out.println("  -m qsim|newsim   simulation to use [default: " + mobsim + "]");
                return;
            } else if (arg.startsWith("-r")) {
                if (arg.length() == 2) {
                    nOfThreadsReplanning = Integer.parseInt(iter.next().trim());
                } else {
                    nOfThreadsReplanning = Integer.parseInt(arg.substring(2).trim());
                }
            } else if (arg.startsWith("-s")) {
                if (arg.length() == 2) {
                    nOfThreadsSim = Integer.parseInt(iter.next().trim());
                } else {
                    nOfThreadsSim = Integer.parseInt(arg.substring(2).trim());
                }
            } else if (arg.startsWith("-e")) {
                if (arg.length() == 2) {
                    nOfThreadsEvents = Integer.parseInt(iter.next().trim());
                } else {
                    nOfThreadsEvents = Integer.parseInt(arg.substring(2).trim());
                }
            } else if (arg.startsWith("-a")) {
                if (arg.length() == 2) {
                    nOfAgents = Integer.parseInt(iter.next().trim());
                } else {
                    nOfAgents = Integer.parseInt(arg.substring(2).trim());
                }
            } else if (arg.startsWith("-f")) {
                if (arg.length() == 2) {
                    flowFactor = Float.parseFloat(iter.next().trim());
                } else {
                    flowFactor = Float.parseFloat(arg.substring(2).trim());
                }
            } else if (arg.startsWith("-o")) {
                if (arg.length() == 2) {
                    outputDir = iter.next().trim();
                } else {
                    outputDir = arg.substring(2).trim();
                }
            } else if (arg.startsWith("-m")) {
                if (arg.length() == 2) {
                    mobsim = iter.next().trim();
                } else {
                    mobsim = arg.substring(2).trim();
                }
            } else {
                System.out.println("Argument " + arg + " not recognized. Ignoring it.");
            }
        }
        System.out.println("MATSim-Benchmark v2");
        System.out.println("# agents: " + nOfAgents);
        System.out.println("# threads replanning: " + nOfThreadsReplanning);
        System.out.println("# threads simulation: " + nOfThreadsSim);
        System.out.println("# threads events:     " + nOfThreadsEvents);
        System.out.println("flow capacity factor: " + flowFactor);
        System.out.println("used mobility sim:    " + mobsim);
        System.out.println("output directory:     " + outputDir);
        File outputDirF = new File(outputDir);
        if (!outputDirF.exists()) {
            outputDirF.mkdir();
        }
        BufferedWriter infoWriter = IOUtils.getBufferedWriter(outputDir + "/info.txt");
        infoWriter.write("MATSim-Benchmark v2\n");
        infoWriter.write("# agents: " + nOfAgents + "\n");
        infoWriter.write("# threads replanning: " + nOfThreadsReplanning + "\n");
        infoWriter.write("# threads simulation: " + nOfThreadsSim + "\n");
        infoWriter.write("# threads events:     " + nOfThreadsEvents + "\n");
        infoWriter.write("flow capacity factor: " + flowFactor + "\n");
        infoWriter.write("used mobility sim:    " + mobsim + "\n");
        infoWriter.write("output directory:     " + outputDir + "\n");
        infoWriter.close();
        BenchmarkV2 app = new BenchmarkV2();
        app.loadNetwork();
        app.createPopulation(nOfAgents, outputDir);
        app.runScenario(nOfThreadsReplanning, nOfThreadsSim, nOfThreadsEvents, flowFactor, outputDir, mobsim);
    }
