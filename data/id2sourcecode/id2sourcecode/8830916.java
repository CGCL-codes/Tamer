    public void run() {
        FacilityLoadReader reader = new FacilityLoadReader();
        reader.readFiles();
        FacilityLoadsWriter writer = new FacilityLoadsWriter();
        writer.write(reader.getFacilityLoads());
    }
