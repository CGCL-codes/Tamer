    public final void printInstances(Network network) throws OrccException {
        write("Printing instances...\n");
        long t0 = System.currentTimeMillis();
        List<Callable<Boolean>> tasks = new ArrayList<Callable<Boolean>>();
        for (final Instance instance : network.getInstances()) {
            tasks.add(new Callable<Boolean>() {

                @Override
                public Boolean call() throws OrccException {
                    return printInstance(instance);
                }
            });
        }
        int numCached = executeTasks(tasks);
        long t1 = System.currentTimeMillis();
        write("Done in " + ((float) (t1 - t0) / (float) 1000) + "s\n");
        if (numCached > 0) {
            write("*******************************************************************************\n");
            write("* NOTE: " + numCached + " instances were not regenerated " + "because they were already up-to-date. *\n");
            write("*******************************************************************************\n");
        }
    }
