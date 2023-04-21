    private void setLogicalChannel(int i) throws FormatException, IOException {
        getMetadataStore().setLogicalChannel(i, getChannelName(i), null, null, null, null, null, null, null, null, null, null, null, getPhotometricInterpretation(i), getMode(i), null, null, null, null, null, getEmWave(i), getExWave(i), null, getNdFilter(i), null);
    }
