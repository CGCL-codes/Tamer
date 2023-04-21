    private void onApply() {
        int m, sh;
        switch(mode.getSelectedIndex()) {
            case 1:
                m = AOPan.FULL_MODE;
                break;
            case 2:
                m = AOPan.MIX_ENDS_MODE;
                break;
            default:
                m = AOPan.HALF_MODE;
                break;
        }
        switch(shape.getSelectedIndex()) {
            case 0:
                sh = AOPan.SQUARE_ROOT_SHAPE;
                break;
            case 2:
                sh = AOPan.SQUARE_SHAPE;
                break;
            default:
                sh = AOPan.LINEAR_SHAPE;
                break;
        }
        ALayerSelection l = getFocussedClip().getSelectedLayer().getSelection();
        ALayerSelection ls = new ALayerSelection(new ALayer());
        ls.addChannelSelection(l.getChannelSelection(0));
        ls.addChannelSelection(l.getChannelSelection(1));
        switch(tab.getSelectedIndex()) {
            case 0:
                ls.operateChannel0WithChannel1(new AOPan(m, sh, (float) pan.getData()));
                break;
            case 1:
                ls.addChannelSelection(panChannel.getSelectedChannel().getSelection());
                ls.operateChannel0WithChannel1WithChannel2(new AOPan(m, sh));
                break;
        }
    }
