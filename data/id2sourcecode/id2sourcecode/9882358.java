    private void onApplyVar() {
        ALayerSelection l = getFocussedClip().getSelectedLayer().getSelection();
        ALayerSelection ls = new ALayerSelection(new ALayer());
        ls.addChannelSelection(l.getChannelSelection(0));
        ls.addChannelSelection(l.getChannelSelection(1));
        ls.addChannelSelection(wideChannel.getSelectedChannel().getSelection());
        ls.operateChannel0WithChannel1WithChannel2(new AONarrowWide(modifyCh1Var.isSelected(), modifyCh2Var.isSelected()));
    }
