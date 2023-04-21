    public static void main(String[] args) {
        LogManager.getLogManager().getLogger("").setLevel(Level.INFO);
        Handler[] handlers = Logger.getLogger("").getHandlers();
        for (int index = 0; index < handlers.length; index++) {
            handlers[index].setLevel(Level.INFO);
            handlers[index].setFormatter(LoggerFormatter.formatter);
        }
        if ((args.length < 6) || ((args.length > 6) && (args.length % 2 != 0))) {
            args = new String[8];
            String bench = "cfrac";
            args[0] = "-profile";
            args[1] = "test" + File.separator + bench + ".mem";
            args[2] = "-method";
            args[3] = "MOGE";
            args[4] = "-grammar";
            args[5] = "test" + File.separator + bench + ".bnf";
            args[6] = "-omode";
            args[7] = "AUTO";
            System.out.println("\nUsage:");
            System.out.println("Explorer -profile <profile> -method <method> -grammar <grammar> [-omode <outmode=MANUAL> -ometrics <metrics> -omap <map> -oaccesses <accesses> -gen <NumberOfGenerations> -indiv <NumberOfIndividuals> -opt <GlobalOptimum>]");
            System.out.println("\nExample: Explorer -profile test" + File.separator + "vdrift.mem -method KINGSLEY -grammar test" + File.separator + "vdrift.bnf");
            System.out.println("where:");
            System.out.println("<profile>: Relative path to the profiling report");
            System.out.println("<method>=KINGSLEY: Evaluates the application using the KINGSLEY DMM");
            System.out.println("<method>=GE: Evaluates the application using Grammatical Evolution (mono-objective)");
            System.out.println("<method>=GE_Front: Evaluates the application using Grammatical Evolution (mono-objective). Returns non dominated solutions.");
            System.out.println("<method>=MOGE: Evaluates the application using Multi Ojective optimization over Grammatical Evolution");
            System.out.println("<grammar>: Relative path to the grammar file");
            System.out.println("<outmode>=MANUAL: Metrics are saved in the files specified by user (if any)");
            System.out.println("<outmode>=AUTO: All metrics are saved and output file names are named automatically (slowest).");
            System.out.println("<metrics>=File path where the main metrics are saved.");
            System.out.println("<map>=File path where a draw of the DMM is saved.");
            System.out.println("<accesses>=File path where the frecuency of (read/write) accesses is saved.\n");
            System.out.println("<NumberOfGenerations>=Number of generations for GE (default is 100)");
            System.out.println("<NumberOfIndividuals>=Number of individuals for GE (default is 60)");
            System.out.println("<GlobalOptimum>=GE stops when GlobalOptimum is reached (default is 0.0)\n");
            return;
        }
        String profile = null, method = null, grammar = null, outmode = "MANUAL", metrics = null, map = null, accesses = null;
        String generations = null, indiv = null, optimum = null;
        for (int i = 0; i < args.length; i += 2) {
            if (args[i].equals("-profile")) {
                profile = args[i + 1];
            } else if (args[i].equals("-method")) {
                method = args[i + 1];
            } else if (args[i].equals("-grammar")) {
                grammar = args[i + 1];
            } else if (args[i].equals("-omode")) {
                outmode = args[i + 1];
            } else if (args[i].equals("-ometrics")) {
                metrics = args[i + 1];
            } else if (args[i].equals("-omap")) {
                map = args[i + 1];
            } else if (args[i].equals("-oaccesses")) {
                accesses = args[i + 1];
            } else if (args[i].equals("-gen")) {
                generations = args[i + 1];
            } else if (args[i].equals("-indiv")) {
                indiv = args[i + 1];
            } else if (args[i].equals("-opt")) {
                optimum = args[i + 1];
            }
        }
        if (outmode.equals("AUTO")) {
            String baseName = null;
            int pos = profile.lastIndexOf(".");
            if (pos < 0) baseName = profile; else baseName = profile.substring(0, pos);
            baseName = baseName + "." + method.toString();
            metrics = baseName + ".mtr";
            map = baseName + ".map";
            accesses = baseName + ".acc";
        }
        DmmExplorerMo explorer = null;
        if (method.equals("KINGSLEY")) {
            explorer = new DmmExplorerMo(profile, DmmExplorerMo.METHOD.KINGSLEY, grammar, metrics, map, accesses);
        } else if (method.equals("GE")) {
            explorer = new DmmExplorerMo(profile, DmmExplorerMo.METHOD.GE, grammar, metrics, map, accesses, generations, indiv, optimum);
        } else if (method.equals("GE_Front")) {
            explorer = new DmmExplorerMo(profile, DmmExplorerMo.METHOD.GE_Front, grammar, metrics, map, accesses, generations, indiv, optimum);
        } else if (method.equals("MOGE")) {
            explorer = new DmmExplorerMo(profile, DmmExplorerMo.METHOD.MOGE, grammar, metrics, map, accesses, generations, indiv, optimum);
        }
        try {
            explorer.explore();
        } catch (FileNotFoundException ex) {
            logger.log(Level.SEVERE, "File not found exception", ex);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "IO Exception", ex);
        }
    }
