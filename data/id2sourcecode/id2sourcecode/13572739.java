    public void testSaveAndLoad() throws Exception {
        this.setUpForLoadSave();
        StringWriter writer = new StringWriter();
        this.hitCounter.save(writer);
        StringReader reader = new StringReader(writer.toString());
        HitCounterPSA loadedHitCounter = HitCounterPSA.load(reader);
        this.testLoaded(loadedHitCounter);
    }
