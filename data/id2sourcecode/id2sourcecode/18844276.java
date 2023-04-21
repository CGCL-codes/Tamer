    public void process(String filename) {
        double start = System.currentTimeMillis();
        Console.println(0, "Saving file " + filename + ".");
        Console.startProgress("Saving file.");
        ONDEXGraph graph = mainFrame.getONDEXGraph();
        if (visible) graph = processVisible(graph);
        try {
            XMLEncoder encoder;
            if (!packed) {
                encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(filename)));
            } else {
                File file = new File(filename);
                ZipEntry zipEntry = new ZipEntry(file.getName());
                ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(file));
                zipOutputStream.putNextEntry(zipEntry);
                encoder = new XMLEncoder(new BufferedOutputStream(zipOutputStream));
            }
            encoder.setPersistenceDelegate(Object2ObjectOpenHashMap.class, encoder.getPersistenceDelegate(Map.class));
            encoder.setPersistenceDelegate(Int2ObjectOpenHashMap.class, encoder.getPersistenceDelegate(Map.class));
            encoder.writeObject(graph);
            encoder.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        Console.println(0, "Saving finished. - " + (System.currentTimeMillis() - start) / 1000 + " s");
        Console.stopProgress();
    }
