    public Double getChannelKnownMaximum(int theC) throws FormatException, IOException {
        FormatTools.assertId(getCurrentFile(), true, 2);
        return chanMax == null ? null : new Double(chanMax[getSeries()][theC]);
    }
