        public void actionPerformed(ActionEvent e) {
            getFocussedClip().getAudio().setAudioListener(eventDispatcher);
            try {
                if (e.getSource() == stopButton) {
                    Debug.println(1, "plugin " + getName() + " [stop] clicked");
                    getFocussedClip().getAudio().stop();
                } else if (e.getSource() == pauseButton) {
                    Debug.println(1, "plugin " + getName() + " [pause] clicked");
                    getFocussedClip().getAudio().pause();
                } else if (e.getSource() == rewButton) {
                    Debug.println(1, "plugin " + getName() + " [rew] clicked");
                    getFocussedClip().getAudio().rewind();
                } else if (e.getSource() == playButton) {
                    try {
                        Debug.println(1, "plugin " + getName() + " [play] clicked");
                        getFocussedClip().getAudio().play();
                    } catch (AudioException ae) {
                        showErrorDialog("audioError", ae.getMessage());
                    }
                } else if (e.getSource() == forwButton) {
                    Debug.println(1, "plugin " + getName() + " [forwind] clicked");
                    getFocussedClip().getAudio().forwind();
                } else if (e.getSource() == recButton) {
                    try {
                        Debug.println(1, "plugin " + getName() + " [rec] clicked");
                        getFocussedClip().getAudio().rec();
                    } catch (AudioException ae) {
                        showErrorDialog("audioError", ae.getMessage());
                    }
                } else if (e.getSource() == loop) {
                    Debug.println(1, "plugin " + getName() + " [loop] clicked");
                    getFocussedClip().getAudio().setLooping(loop.isSelected());
                } else if (e.getSource() == autoGrow) {
                    Debug.println(1, "plugin " + getName() + " [autoGrow] clicked");
                    getFocussedClip().getAudio().setAutoGrowing(autoGrow.isSelected());
                    reload();
                } else if (e.getSource() == loopPointerSettings) {
                    Debug.println(1, "plugin " + getName() + " [loop pointer settings] clicked");
                    AClip c = getFocussedClip();
                    Audio a = c.getAudio();
                    AChannelPlotter p = c.getSelectedLayer().getChannel(0).getPlotter();
                    AChannelSelection s = c.getSelectedLayer().getChannel(0).getSelection();
                    switch(loopPointerSettings.getSelectedIndex()) {
                        case 0:
                            a.setLoopStartPointer(0);
                            a.setLoopEndPointer(c.getMaxSampleLength());
                            break;
                        case 1:
                            a.setLoopStartPointer((int) p.getXOffset());
                            a.setLoopEndPointer((int) (p.getXOffset() + p.getXLength()));
                            break;
                        case 2:
                            a.setLoopStartPointer((int) s.getOffset());
                            a.setLoopEndPointer((int) (s.getOffset() + s.getLength()));
                            break;
                        case 3:
                            a.setLoopStartPointer((int) GPMeasure.getLowerCursor());
                            a.setLoopEndPointer((int) GPMeasure.getHigherCursor());
                            break;
                    }
                    repaintFocussedClipEditor();
                }
            } catch (NullPointerException npe) {
            }
            updateButtons();
        }
