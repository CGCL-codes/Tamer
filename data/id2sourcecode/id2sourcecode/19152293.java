    public static void main(String args[]) throws Exception {
        String loc_str = System.getProperty("java.class.path");
        String path_separator = System.getProperty("path.separator");
        StringTokenizer strTok = new StringTokenizer(loc_str, path_separator);
        String[] locs = new String[strTok.countTokens()];
        for (int i = 0; strTok.hasMoreElements(); i++) {
            locs[i] = strTok.nextToken();
        }
        Properties chxProps = getProperties();
        loc_str = chxProps.getProperty("locations");
        strTok = new StringTokenizer(loc_str, path_separator);
        String[] locs2 = new String[locs.length + strTok.countTokens()];
        for (int i = 0; i < locs.length; i++) {
            locs2[i] = locs[i];
        }
        for (int i = locs.length; strTok.hasMoreElements(); i++) {
            locs2[i] = strTok.nextToken();
        }
        Node node = TreeBuilder.buildTree(locs2);
        ClassLoader cloader = new ChxClassLoader(node);
        String[] prog_args = new String[args.length - 1];
        for (int i = 0; i < prog_args.length; i++) {
            prog_args[i] = args[i + 1];
        }
        TreeViewer viewer = new TreeViewer(node);
        JFrame frame = new JFrame("Tree Viewer");
        frame.getContentPane().add(viewer, BorderLayout.CENTER);
        JButton go = new JButton("Go");
        go.addActionListener(new Main(args[0], prog_args, cloader));
        frame.getContentPane().add(go, BorderLayout.SOUTH);
        frame.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        frame.setSize(300, 400);
        frame.show();
    }
