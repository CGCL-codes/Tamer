    private JPopupMenu getLocalMenu(final DirectoryNode fNode) {
        JPopupMenu popup = new JPopupMenu();
        JMenuItem menuitem = new JMenuItem("Save file to...");
        menuitem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                String targetFile = Utils.selectFile("Save file as", fNode.getName(), (JFrame) Main.getUI());
                try {
                    FileUtils.copyFile(fNode.getLocalURL(), new File(targetFile));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        popup.add(menuitem);
        menuitem = new JMenuItem("View as text");
        menuitem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                Window window = new Window((Frame) null, "Content of file " + fNode.getName());
                JTextArea ta = new JTextArea();
                Utils.addPopupMenu(ta);
                ta.setEditable(false);
                try {
                    ta.setText(FileUtils.readFile(fNode.getLocalURL()));
                } catch (IOException ioe) {
                    ta.setText("Error: File " + fNode.getLocalURL() + " cannot be found");
                }
                window.setContent(new JScrollPane(ta));
                window.addCloseButton();
                window.showWindow();
            }
        });
        popup.add(menuitem);
        return popup;
    }
