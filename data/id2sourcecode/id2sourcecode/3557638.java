    private final void reloadCache() {
        AChannel ch = getChannelModel();
        if (!oldId.equals(ch.getChangeId()) || oldWindowType != windowType || oldFftLength != fftLength) {
            int k = 0;
            LProgressViewer.getInstance().entrySubProgress(GLanguage.translate("reloadCache") + " " + model.getName());
            LProgressViewer.getInstance().entrySubProgress(0.9);
            oldId = ch.getChangeId();
            oldWindowType = windowType;
            oldFftLength = fftLength;
            Debug.println(7, "reload cache of channel spectrogram plotter name=" + ch.getName() + " id=" + ch.getChangeId());
            MMArray s = getChannelModel().getSamples();
            int bl = s.getLength() / fftLength;
            if (cacheMag == null) {
                cacheMag = new MMArray(s.getLength() / 2, 0);
            }
            if (cacheMag.getLength() != s.getLength() / 2) {
                cacheMag.setLength(s.getLength() / 2);
            }
            magMin = Float.MAX_VALUE;
            magMax = Float.MIN_VALUE;
            if (re == null) {
                re = new MMArray(fftLength, 0);
            }
            if (re.getLength() != fftLength) {
                re.setLength(fftLength);
            }
            if (im == null) {
                im = new MMArray(fftLength, 0);
            }
            if (im.getLength() != fftLength) {
                im.setLength(fftLength);
            }
            for (int i = 0; i < bl + 1; i++) {
                LProgressViewer.getInstance().setProgress((i + 1) / bl + 1);
                int ii = i * fftLength / 2;
                re.copy(s, ii * 2, 0, fftLength);
                im.clear();
                AOToolkit.applyFFTWindow(windowType, re, fftLength);
                AOToolkit.complexFft(re, im);
                for (int j = 0; j < fftLength / 2; j++) {
                    float m = AOToolkit.cartesianToMagnitude(re.get(j), im.get(j));
                    cacheMag.set(ii + j, m);
                    magMin = Math.min(magMin, m);
                    magMax = Math.max(magMax, m);
                    magMean += m;
                    k++;
                }
            }
            magRange = Math.max(magMax - magMin, 0.0000001f);
            magMean /= k;
            magMean = (magMean - magMin) / magRange;
            LProgressViewer.getInstance().exitSubProgress();
            LProgressViewer.getInstance().exitSubProgress();
        }
    }
