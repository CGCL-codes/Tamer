    private void onUpdate() {
        AOSpectrum s = new AOSpectrum(bufferLength.getSelectedValue(), window.getSelectedIndex());
        ALayerSelection ls = getFocussedClip().getSelectedLayer().getSelection();
        ls.operateEachChannel(s);
        AClip c = spectrumClipEditor.getClip();
        AChannel ch = c.getLayer(0).getChannel(0);
        ch.setSamples(s.getSpectrum());
        ch.markChange();
        try {
            c.getHistory().store(loadIcon(), GLanguage.translate(getName()));
        } catch (NullPointerException npe) {
        }
        if (autoscale.isSelected()) {
            c.getPlotter().autoScale();
        }
        spectrumClipEditor.reload();
    }
