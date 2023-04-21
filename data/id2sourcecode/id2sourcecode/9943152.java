    public SearchPanel() {
        searchURL = VueResources.getString("url.google");
        XML_MAPPING = VueResources.getString("mapping.google");
        this.setLayout(new BorderLayout());
        JPanel queryPanel = new JPanel();
        final JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BorderLayout());
        queryPanel.setLayout(new BorderLayout());
        final JTextField queryBox = new JTextField();
        queryPanel.add(queryBox, BorderLayout.NORTH);
        JButton searchButton = new JButton("Search");
        searchButton.setPreferredSize(new Dimension(100, 30));
        queryPanel.add(searchButton, BorderLayout.SOUTH);
        this.add(queryPanel, BorderLayout.NORTH);
        this.add(resultPanel, BorderLayout.CENTER);
        searchButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int index = 0;
                JScrollPane jsp = new JScrollPane();
                String searchString = queryBox.getText();
                if (!searchString.equals("")) {
                    try {
                        url = new URL(searchURL + searchString);
                        InputStream input = url.openStream();
                        int c;
                        while ((c = input.read()) != -1) {
                            result = result + (char) c;
                        }
                        FileWriter fileWriter = new FileWriter("google_result.xml");
                        fileWriter.write(result);
                        fileWriter.close();
                        result = "";
                        GSP gsp = loadGSP("google_result.xml");
                        Iterator i = gsp.getRES().getResultList().iterator();
                        Vector resultVector = new Vector();
                        while (i.hasNext()) {
                            Result r = (Result) i.next();
                            resultVector.add(r.getUrl());
                            System.out.println(r.getTitle() + " " + r.getUrl());
                        }
                        VueDragTree tree = new VueDragTree(resultVector.iterator(), "GoogleSearchResults");
                        tree.setEditable(true);
                        tree.setRootVisible(false);
                        JTextField ja = new JTextField("Google Search Results");
                        jsp = new JScrollPane(tree);
                        resultPanel.add(ja, BorderLayout.NORTH);
                        resultPanel.add(jsp, BorderLayout.CENTER, index);
                        index = index++;
                        resultPanel.revalidate();
                    } catch (Exception ex) {
                    }
                }
            }
        });
    }
