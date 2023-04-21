    public JScrollPane createTree() {
        DefaultMutableTreeNode top = new DefaultMutableTreeNode(getString("TreeDemo.music"));
        DefaultMutableTreeNode catagory = null;
        DefaultMutableTreeNode artist = null;
        DefaultMutableTreeNode record = null;
        URL url = getClass().getResource("/resources/tree.txt");
        try {
            InputStream is = url.openStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(isr);
            String line = reader.readLine();
            while (line != null) {
                char linetype = line.charAt(0);
                switch(linetype) {
                    case 'C':
                        catagory = new DefaultMutableTreeNode(line.substring(2));
                        top.add(catagory);
                        break;
                    case 'A':
                        if (catagory != null) {
                            catagory.add(artist = new DefaultMutableTreeNode(line.substring(2)));
                        }
                        break;
                    case 'R':
                        if (artist != null) {
                            artist.add(record = new DefaultMutableTreeNode(line.substring(2)));
                        }
                        break;
                    case 'S':
                        if (record != null) {
                            record.add(new DefaultMutableTreeNode(line.substring(2)));
                        }
                        break;
                    default:
                        break;
                }
                line = reader.readLine();
            }
        } catch (IOException e) {
        }
        tree = new JTree(top) {

            public Insets getInsets() {
                return new Insets(5, 5, 5, 5);
            }
        };
        tree.setName("TreeDemo.tree");
        tree.setEditable(true);
        return new JScrollPane(tree);
    }
