    public DemoRunner(final ZirkelCanvas zc, final ZirkelApplet za, final String filename, final Label label) {
        ZC = zc;
        ZA = za;
        L = label;
        try {
            URL url;
            if (filename.toUpperCase().startsWith("HTTP")) url = new URL(filename); else url = new URL(ZA.getCodeBase(), filename);
            final InputStream in = url.openStream();
            final XmlReader xml = new XmlReader();
            xml.init(in);
            XmlTree tree = xml.scan();
            Enumeration e = tree.getContent();
            while (e.hasMoreElements()) {
                tree = (XmlTree) e.nextElement();
                if (tree.getTag() instanceof XmlTagPI) continue;
                if (!tree.getTag().name().equals("Demo")) throw new ConstructionException("Demo tag not found"); else {
                    final XmlTag tag = tree.getTag();
                    if (tag.hasParam("delay")) {
                        try {
                            delay = Integer.parseInt(tag.getValue("delay"));
                        } catch (final Exception ex) {
                        }
                    }
                    break;
                }
            }
            Tree = tree;
            e = tree.getContent();
            if (!e.hasMoreElements()) return;
            while (e.hasMoreElements()) {
                tree = (XmlTree) e.nextElement();
                if (!tree.getTag().name().equals("File")) throw new ConstructionException("Illegal tag " + tree.getTag().name());
            }
            in.close();
        } catch (final ConstructionException e) {
            label.setText(e.toString());
        } catch (final Exception e) {
            label.setText("Error loading " + filename);
        }
        zc.addMouseListener(this);
        new Thread(this).start();
    }
