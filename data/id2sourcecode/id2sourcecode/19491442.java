        public HistoryElement(ImageIcon icon, String d) {
            this.icon = icon;
            time = dateFormat.format(new Date());
            description = d;
            if (historyEnable) {
                clip.markChange();
                file = new File(historyPath + clip.getChangeId() + historyExtension);
                file.deleteOnExit();
                try {
                    LProgressViewer.getInstance().entrySubProgress(0.5, "storeToHistory", false);
                    Debug.println(3, "history save clip " + clip.getName() + " to file " + file.getName());
                    AClipStorage.saveWithoutSamples(clip, file);
                    for (int i = 0; i < clip.getNumberOfLayers(); i++) {
                        if (LProgressViewer.getInstance().setProgress((i + 1) * 100 / clip.getNumberOfLayers())) return;
                        ALayer l = clip.getLayer(i);
                        LProgressViewer.getInstance().entrySubProgress(0.3, "", false);
                        for (int j = 0; j < l.getNumberOfChannels(); j++) {
                            if (LProgressViewer.getInstance().setProgress((j + 1) * 100 / l.getNumberOfChannels())) return;
                            AChannel ch = l.getChannel(j);
                            File chFile = new File(historyPath + ch.getChangeId() + historyExtension);
                            if (!chFile.exists()) {
                                Debug.println(3, "history save channel " + ch.getName() + " to file " + chFile.getName());
                                chFile.deleteOnExit();
                                AClipStorage.saveSamples(ch, chFile);
                            }
                        }
                        LProgressViewer.getInstance().exitSubProgress();
                    }
                    LProgressViewer.getInstance().exitSubProgress();
                } catch (IOException ioe) {
                    Debug.printStackTrace(5, ioe);
                }
            }
        }
