    private void onApplyConst() {
        if (baseSignalClipEditor != null) {
            MMArray sig = baseSignalClipEditor.getClip().getLayer(0).getChannel(0).getSamples();
            ALayerSelection ls = getFocussedClip().getSelectedLayer().getSelection();
            ls.operateEachChannel(new AOPitchGenerator(sig, (float) constPitch.getData()));
        }
    }
