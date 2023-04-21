    public int[] getChannelDimLengths() {
        FormatTools.assertId(currentId, true, 1);
        return intensity ? new int[] { channels } : new int[] { timeBins, channels };
    }
