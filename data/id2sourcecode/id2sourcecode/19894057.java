        public void actionPerformed(ActionEvent e) {
            AChannelMask m = getFocussedClip().getSelectedLayer().getSelectedChannel().getMask();
            if (e.getSource() == clear) {
                Debug.println(1, "plugin " + getName() + " [clear] clicked");
                m.clear();
                wideMaskCopy(GToolkit.isCtrlKey(e), m);
                repaintFocussedClipEditor();
            } else if (e.getSource() == complementary) {
                Debug.println(1, "plugin " + getName() + " [complementary] clicked");
                m.setComplementary();
                wideMaskCopy(GToolkit.isCtrlKey(e), m);
                repaintFocussedClipEditor();
            } else if (e.getSource() == applyDefinitely) {
                LProgressViewer.getInstance().entrySubProgress(getName());
                LProgressViewer.getInstance().entrySubProgress(0.7);
                Debug.println(1, "plugin " + getName() + " [apply definitely] clicked");
                if (GToolkit.isCtrlKey(e)) {
                    ALayer l = getFocussedClip().getSelectedLayer();
                    for (int i = 0; i < l.getNumberOfChannels(); i++) {
                        l.getChannel(i).getMask().applyDefinitely();
                    }
                } else {
                    m.applyDefinitely();
                }
                updateHistory(GLanguage.translate(getName()));
                LProgressViewer.getInstance().exitSubProgress();
                LProgressViewer.getInstance().exitSubProgress();
                reloadFocussedClipEditor();
            }
            updateHistory(GLanguage.translate(getName()));
        }
