    public static void main(String[] args) throws NumberFormatException, IOException {
        Logger.getLogger("").setLevel(Level.WARNING);
        final int nbVars = Integer.valueOf(args[0]);
        final int domainSize = Integer.valueOf(args[1]);
        final List<Class<? extends Filter>> list = new ArrayList<Class<? extends Filter>>();
        list.add(SAC1.class);
        final FileWriter fw = new FileWriter("threshold_" + nbVars + "_" + domainSize + ".dat");
        fw.write("constraints");
        for (Class<? extends Filter> c : list) {
            fw.write("\t" + c.getSimpleName());
        }
        fw.write("\n");
        for (int e = 945; e <= (nbVars * (nbVars - 1)) / 2; e += 20) {
            fw.write(Integer.toString(e));
            for (Class<? extends Filter> clazz : list) {
                double lb = .62;
                double ub = .65;
                while (ub - lb > .0005) {
                    final double test = (ub + lb) / 2;
                    System.out.print("[" + lb + ", " + ub + "] " + test + ": ");
                    final int unsat = new RB(nbVars, domainSize, 2, e, Tightness.PROBABILITY, test, Structure.UNSTRUCTURED, Structure.UNSTRUCTURED, false, false, INST, 0).runFilter(clazz);
                    System.out.println(unsat);
                    if (unsat <= INST / 2) {
                        lb = test;
                    } else {
                        ub = test;
                    }
                }
                fw.write("\t" + 1000 * (lb + ub) / 2);
                fw.flush();
            }
            fw.write("\n");
        }
        fw.close();
    }
