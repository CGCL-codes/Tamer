    public Double getChannelGlobalMaximum(int theC) throws FormatException, IOException {
        FormatTools.assertId(getCurrentFile(), true, 2);
        if (theC < 0 || theC >= getSizeC()) {
            throw new FormatException("Invalid channel index: " + theC);
        }
        int series = getSeries();
        if (minMaxDone == null || minMaxDone[series] < getImageCount()) {
            return null;
        }
        return new Double(chanMax[series][theC]);
    }
