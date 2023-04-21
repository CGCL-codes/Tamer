    public static void main(String[] args) throws FailedGenerationException, NumberFormatException, IOException, ClassNotFoundException {
        ParameterManager.parse("heuristic.variable", "cspfj.heuristic.WDegFixedOnDom");
        for (int i : Arrays.asList(4, 8, 12, 15, 20, 30, 50, 80, 100, 120, 150)) {
            System.out.println(i + " :");
            long time = -System.currentTimeMillis();
            final Queens queens = new Queens(i);
            final CSPOM problem = queens.generate();
            ProblemCompiler.compile(problem);
            final Solver solver = Solver.factory(problem);
            solver.nextSolution();
            System.out.println((System.currentTimeMillis() + time) / 1000f);
            System.out.println(solver.statistics().digest());
        }
    }
