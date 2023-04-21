    public Simulator(String[] args) {
        super("simulator" + theirPopulation);
        int numpoints = Integer.parseInt(args[0]);
        int minseconds = Integer.parseInt(args[1]);
        int maxseconds = Integer.parseInt(args[2]);
        System.out.println("Simulator: Will create " + numpoints + " dummy points");
        for (int i = 0; i < numpoints; i++) {
            String[] names = { "dummy." + i };
            String[] empty = { "-" };
            String[] transaction = { "Generic-\"simulator" + theirPopulation + "\"" };
            String[] translation = { "NumDecimals-\"2\"" };
            String[] arch = { "-" };
            int period = minseconds + itsRandom.nextInt(maxseconds - minseconds);
            PointDescription pd = PointDescription.factory(names, "Dummy " + i, "Dummy" + i, "", "sim" + theirPopulation, transaction, empty, translation, empty, arch, "" + period + "000000", "-1", true);
            pd.populateServerFields();
        }
        theirPopulation++;
    }
