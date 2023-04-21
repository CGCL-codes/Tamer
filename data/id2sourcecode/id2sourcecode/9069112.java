    public void render(MultiModeContext mmc, int targetChannel) {
        MultiModeChannelSelection[] chData = getChannelData();
        Integer val;
        Integer ch;
        int numChnls = (mmc.has32Channels() ? 32 : 16);
        for (int i = 0, n = chData.length; i < n; i++) {
            if (targetChannel + i > numChnls) break;
            ch = IntPool.get(targetChannel + i);
            val = chData[i].getPreset();
            if (val != null) try {
                mmc.setPreset(ch, val).post();
            } catch (ResourceUnavailableException e) {
                e.printStackTrace();
            }
            val = chData[i].getVolume();
            if (val != null) try {
                mmc.setVolume(ch, val).post();
            } catch (ResourceUnavailableException e) {
                e.printStackTrace();
            }
            val = chData[i].getPan();
            if (val != null) try {
                mmc.setPan(ch, val).post();
            } catch (ResourceUnavailableException e) {
                e.printStackTrace();
            }
            val = chData[i].getSubmix();
            if (val != null) try {
                mmc.setSubmix(ch, val).post();
            } catch (ResourceUnavailableException e) {
                e.printStackTrace();
            }
        }
    }
