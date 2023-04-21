    private static void reduceNetwork() {
        double xCenter = (640000 + 740000) / 2;
        double yCenter = (200000 + 310000) / 2;
        NetworkReducer reducer = new NetworkReducer(xCenter, yCenter, 100000);
        reducer.run(network);
        NetworkCleaner cleaner = new NetworkCleaner();
        cleaner.run(network);
        NetworkWriter network_writer = new NetworkWriter(network);
        network_writer.write();
        System.out.println("Wrote network to " + Gbl.getConfig().network().getOutputFile());
    }
