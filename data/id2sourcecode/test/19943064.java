    public String[] getChannelDimTypes() {
        FormatTools.assertId(currentId, true, 1);
        return intensity ? new String[] { FormatTools.SPECTRA } : new String[] { FormatTools.LIFETIME, FormatTools.SPECTRA };
    }
