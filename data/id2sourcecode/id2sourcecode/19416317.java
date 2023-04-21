    public static void main(String[] args) {
        BDBStore rdfStore = null;
        try {
            URL url = new URL("http://archive.geneontology.org/latest-lite/go_20090621-termdb.owl.gz");
            rdfStore = new BDBStore();
            rdfStore.open(new File("/tmp/rdfdb"));
            rdfStore.clear();
            InputStream in = new GZIPInputStream(url.openStream());
            rdfStore.read(in);
            in.close();
            System.out.println("Done");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rdfStore != null) try {
                rdfStore.close();
            } catch (Exception err) {
                err.printStackTrace();
            }
        }
    }
