    private void onApplyVar() {
        if (baseSignalClipEditor != null) {
            MMArray sig = baseSignalClipEditor.getClip().getLayer(0).getChannel(0).getSamples();
            AClipSelection cs = new AClipSelection(new AClip());
            cs.addLayerSelection(getFocussedClip().getSelectedLayer().getSelection());
            cs.addLayerSelection(layerChooser.getSelectedLayer().createSelection());
            cs.operateLayer0WithLayer1(new AOPitchGenerator(sig));
        }
    }
