    public static void main(final String[] args) throws IOException, FailedGenerationException {
        Logger.getLogger("").setLevel(Level.INFO);
        Logger.getLogger("").getHandlers()[0].setLevel(Level.INFO);
        final Options opt = new Options();
        opt.addOption("lb", true, "Lower Bound");
        opt.addOption("ub", true, "Upper Bound");
        opt.addOption("js", false, "Job Shop");
        final CommandLine cl;
        try {
            cl = new GnuParser().parse(opt, args);
        } catch (ParseException e) {
            final HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("", opt);
            System.exit(1);
            throw new InvalidParameterException();
        }
        final OpenShopGenerator generator;
        switch(cl.getArgs().length) {
            case 1:
                generator = new OpenShopGenerator(cl.getArgs()[0]);
                break;
            case 3:
                generator = new OpenShopGenerator(Integer.valueOf(cl.getArgs()[0]), Integer.valueOf(cl.getArgs()[1]), Integer.valueOf(cl.getArgs()[2]), cl.hasOption("js"));
                break;
            default:
                final HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("OpenShop { filename | size durationSeed machineSeed }", opt);
                System.exit(1);
                throw new InvalidParameterException();
        }
        System.out.println(generator);
        int lb;
        if (cl.hasOption("lb")) {
            lb = Integer.valueOf(cl.getOptionValue("lb"));
        } else {
            lb = generator.getLB();
        }
        int ub;
        if (cl.hasOption("ub")) {
            ub = Integer.valueOf(cl.getOptionValue("ub"));
        } else {
            ub = generator.getUB();
        }
        long totalTime = 0;
        while (ub > lb) {
            System.out.println("[" + lb + "," + ub + "]");
            final int test = (ub + lb) / 2;
            System.out.println("Test " + test);
            long time = -System.currentTimeMillis();
            generator.setUB(test);
            final CSPOM cspom = generator.generate();
            ProblemCompiler.compile(cspom);
            final Solver solver = Solver.factory(cspom);
            final Option<Map<String, Number>> solution = solver.nextSolutionNum();
            time += System.currentTimeMillis();
            totalTime += time;
            System.out.print("In " + time / 1000f + ": ");
            if (!solution.isDefined()) {
                System.out.println("UNSAT");
                lb = test + 1;
            } else {
                final Set<CSPOMConstraint> control = cspom.control(solution.get());
                if (control.size() > 0) {
                    throw new IllegalStateException(control.toString());
                }
                ub = generator.evaluate(solution.get());
                System.out.println(ub);
            }
            System.out.println(solver.statistics().digest());
            System.out.println();
        }
        System.out.println(ub + "! (" + totalTime / 1000f + ")");
    }
