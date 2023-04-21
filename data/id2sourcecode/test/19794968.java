    public Double getChannelKnownMinimum(int theC) throws FormatException, IOException {
        FormatTools.assertId(getCurrentFile(), true, 2);
        return chanMin == null ? null : new Double(chanMin[getSeries()][theC]);
    }
