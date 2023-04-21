    public void loadChromosomeTable(CTable table, CMessageWriter writer) {
        CChromosomeTableReader reader = new CChromosomeTableReader(this, writer);
        reader.loadTable(table);
    }
