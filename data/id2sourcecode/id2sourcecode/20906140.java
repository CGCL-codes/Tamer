    public void loadFeatureTable(CTable table, CMessageWriter writer) {
        CFeatureTableReader reader = new CFeatureTableReader(this, writer);
        reader.loadTable(table);
    }
