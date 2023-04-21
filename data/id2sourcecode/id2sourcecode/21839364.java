    public int getChannelCount(TreeNode trak) {
        AudioSampleEntry ase = (AudioSampleEntry) getSampleEntry(trak, 0);
        return ase.channelcount;
    }
