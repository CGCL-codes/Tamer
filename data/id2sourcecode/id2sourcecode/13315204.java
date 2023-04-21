    public void mouseDragged(MouseEvent e) {
        if (selectedPointer != NO_POINTER) {
            AClip c = ((GClipPanel) e.getSource()).getClip();
            int x = (int) c.getSelectedLayer().getChannel(0).getPlotter().graphToSampleX(e.getPoint().x);
            switch(selectedPointer) {
                case LOOP_START_POINTER:
                    c.getAudio().setLoopStartPointer(x);
                    break;
                case LOOP_END_POINTER:
                    c.getAudio().setLoopEndPointer(x);
                    break;
            }
            repaintFocussedClipEditor();
        }
    }
