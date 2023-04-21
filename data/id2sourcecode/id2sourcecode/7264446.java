    private void onApply() {
        ALayer l = getSelectedLayer();
        for (int i = 0; i < l.getNumberOfChannels(); i++) {
            GEditableSegments s = ((GCookieFreeGenerator) l.getChannel(i).getCookies().getCookie(getName())).segments;
            GEditableFreehand f = ((GCookieFreeGenerator) l.getChannel(i).getCookies().getCookie(getName())).freehand;
            int o = 0;
            switch(operation.getSelectedIndex()) {
                case 0:
                    o = AOSegmentGenerator.REPLACE_OPERATION;
                    break;
                case 1:
                    o = AOSegmentGenerator.ENVELOPE_OPERATION;
                    break;
            }
            switch(tab.getSelectedIndex()) {
                case 0:
                    if (selectionIndependent.isSelected()) {
                        s.convertToSamples(o);
                    } else {
                        s.convertToSelectedSamples(o);
                    }
                    break;
                case 1:
                    if (selectionIndependent.isSelected()) {
                        f.convertToSamples(o);
                    } else {
                        f.convertToSelectedSamples(o);
                    }
                    break;
            }
        }
    }
