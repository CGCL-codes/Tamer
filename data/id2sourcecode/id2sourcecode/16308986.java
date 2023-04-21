    public static void main(String[] args) {
        try {
            URL url = new URL("http://www.nexml.org/nexml/examples/tolweb.xml");
            InputStream in = url.openStream();
            NexmlIO io = new NexmlIO(PhyloTree.class);
            RootedTree tree = io.parseStream(in);
            System.out.println(tree.getNewick());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
