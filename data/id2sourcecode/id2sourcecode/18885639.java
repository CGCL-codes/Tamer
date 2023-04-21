    public void run() {
        Enumeration e = Tree.getContent();
        ZC.setFrozen(true);
        while (true) {
            Continue = false;
            final int D = delay;
            if (e.hasMoreElements()) {
                try {
                    XmlTree tree = (XmlTree) e.nextElement();
                    final XmlTag tag = tree.getTag();
                    final String filename = tag.getValue("name");
                    URL url;
                    if (filename.toUpperCase().startsWith("HTTP")) url = new URL(filename); else url = new URL(ZA.getCodeBase(), filename);
                    final InputStream in = url.openStream();
                    ZC.clearMacros();
                    ZC.load(in);
                    in.close();
                    ZC.recompute();
                    if (tag.hasParam("delay")) {
                        try {
                            delay = Integer.parseInt(tag.getValue("delay"));
                        } catch (final Exception ex) {
                        }
                    }
                    final Enumeration en = tree.getContent();
                    while (en.hasMoreElements()) {
                        tree = (XmlTree) en.nextElement();
                        if (tree.getTag() instanceof XmlTagText) {
                            L.setText(((XmlTagText) tree.getTag()).getContent());
                        }
                    }
                    startZC();
                } catch (final Exception ex) {
                    L.setText("Error loading file!");
                }
                try {
                    for (int i = 0; i < delay * 2 || Hold; i++) {
                        Thread.sleep(500);
                        if (i == 0) {
                            ZC.setFrozen(false);
                            ZC.repaint();
                        }
                        if (Stopped) return;
                        if (Continue) {
                            Hold = false;
                            break;
                        }
                    }
                    ZC.setFrozen(true);
                } catch (final Exception ex) {
                }
                delay = D;
            } else {
                e = Tree.getContent();
            }
        }
    }
