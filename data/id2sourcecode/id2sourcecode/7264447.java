    private void onInterpolationChange() {
        ALayer l = getSelectedLayer();
        try {
            for (int i = 0; i < l.getNumberOfChannels(); i++) {
                GEditableSegments s = ((GCookieFreeGenerator) l.getChannel(i).getCookies().getCookie(getName())).segments;
                switch(tab.getSelectedIndex()) {
                    case 0:
                        switch(interpolation.getSelectedIndex()) {
                            case 0:
                                s.setSegmentMode(GEditableSegments.SINGLE_POINTS);
                                break;
                            case 1:
                                s.setSegmentMode(GEditableSegments.ORDER_0);
                                break;
                            case 2:
                                s.setSegmentMode(GEditableSegments.ORDER_1);
                                break;
                            case 3:
                                s.setSegmentMode(GEditableSegments.SPLINE);
                                break;
                        }
                        break;
                    case 1:
                        break;
                }
            }
        } catch (Exception e) {
        }
    }
