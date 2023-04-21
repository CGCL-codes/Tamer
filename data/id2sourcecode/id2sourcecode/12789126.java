    public static RootedTree parseFile(RootedTree t, File f) {
        try {
            URI uri = f.toURI();
            URL url = uri.toURL();
            InputStream is = url.openStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            if (t instanceof PhyloTree) {
                PhyloTree pt = (PhyloTree) t;
                String str = f.getParent();
                pt.setBaseURL(str);
            }
            return parseReader(t, br);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
