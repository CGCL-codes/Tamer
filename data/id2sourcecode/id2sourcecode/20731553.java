    public final void printEntities(Network network) throws OrccException {
        write("Printing entities...\n");
        long t0 = System.currentTimeMillis();
        List<Callable<Boolean>> tasks = new ArrayList<Callable<Boolean>>();
        for (final Entity entity : network.getEntities()) {
            tasks.add(new Callable<Boolean>() {

                @Override
                public Boolean call() throws OrccException {
                    if (isCanceled()) {
                        return false;
                    }
                    return printEntity(entity);
                }
            });
        }
        int numCached = executeTasks(tasks);
        long t1 = System.currentTimeMillis();
        write("Done in " + ((float) (t1 - t0) / (float) 1000) + "s\n");
        if (numCached > 0) {
            write("*******************************************************************************\n");
            write("* NOTE: " + numCached + " entities were not regenerated " + "because they were already up-to-date. *\n");
            write("*******************************************************************************\n");
        }
    }
