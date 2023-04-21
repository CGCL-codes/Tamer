    private final void reloadCache() {
        AChannel ch = getChannelModel();
        if (!oldId.equals(ch.getChangeId())) {
            LProgressViewer.getInstance().entrySubProgress(GLanguage.translate("reloadCache") + " " + model.getName());
            LProgressViewer.getInstance().entrySubProgress(0.9);
            Debug.println(7, "reload cache of channel sample plotter name=" + ch.getName() + " id=" + ch.getChangeId() + " oldid=" + oldId);
            oldId = ch.getChangeId();
            if (cachedMin == null) {
                cachedMin = new MMArray(ch.getSampleLength() / reduction, 0);
            }
            cachedMin.setLength(ch.getSampleLength() / reduction);
            if (cachedMax == null) {
                cachedMax = new MMArray(ch.getSampleLength() / reduction, 0);
            }
            cachedMax.setLength(ch.getSampleLength() / reduction);
            for (int i = 0; i < cachedMin.getLength(); i++) {
                if (i % 10000 == 0) LProgressViewer.getInstance().setProgress((i + 1) / cachedMin.getLength());
                cachedMin.set(i, getMinSample(i * reduction, (i + 1) * reduction));
                cachedMax.set(i, getMaxSample(i * reduction, (i + 1) * reduction));
            }
            LProgressViewer.getInstance().exitSubProgress();
            LProgressViewer.getInstance().exitSubProgress();
        }
    }
