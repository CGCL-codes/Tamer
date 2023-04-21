        public AClip reloadClip(AClip c) {
            if (historyEnable && (c != null)) {
                try {
                    LProgressViewer.getInstance().entrySubProgress(0.5, "reloadFromHistory");
                    Map<String, AChannel> chm = new HashMap<String, AChannel>();
                    for (int i = 0; i < c.getNumberOfLayers(); i++) {
                        for (int j = 0; j < c.getLayer(i).getNumberOfChannels(); j++) {
                            chm.put(c.getLayer(i).getChannel(j).getChangeId(), c.getLayer(i).getChannel(j));
                        }
                    }
                    c.removeAll();
                    AClipStorage.load(c, file);
                    Debug.println(3, "history load clip" + clip.getName() + " from file " + file.getName());
                    for (int i = 0; i < c.getNumberOfLayers(); i++) {
                        if (LProgressViewer.getInstance().setProgress((i + 1) * 100 / c.getNumberOfLayers())) return c;
                        ALayer l = c.getLayer(i);
                        LProgressViewer.getInstance().entrySubProgress(0.3);
                        for (int j = 0; j < l.getNumberOfChannels(); j++) {
                            if (LProgressViewer.getInstance().setProgress((j + 1) * 100 / l.getNumberOfChannels())) return c;
                            AChannel ch = l.getChannel(j);
                            if (chm.containsKey(ch.getChangeId())) {
                                Debug.println(13, "history reuse channel, id=" + ch.getChangeId());
                                ch.setSamples(chm.get(ch.getChangeId()).getSamples());
                            } else {
                                Debug.println(13, "history reload channel, id=" + ch.getChangeId());
                                File chFile = new File(historyPath + ch.getChangeId() + historyExtension);
                                if (chFile.exists()) {
                                    ch.setSamples(AClipStorage.loadSamples(chFile));
                                }
                            }
                        }
                        LProgressViewer.getInstance().exitSubProgress();
                    }
                    chm.clear();
                    LProgressViewer.getInstance().exitSubProgress();
                } catch (IOException ioe) {
                    Debug.printStackTrace(5, ioe);
                }
            }
            return c;
        }
