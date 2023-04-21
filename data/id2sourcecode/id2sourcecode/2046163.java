    void generateHitList(URL connectURL, final String keyword) {
        final int page = 0;
        int matchingDocCount = 0;
        JTabbedPane tabs = new JTabbedPane();
        GridBagLayout gbLayout = null;
        Vector linkVector = new Vector();
        final Vector commentVector = new Vector();
        JScrollPane scrollPane = null;
        JPanel hitPanel = null;
        String line = null, link, content, label, head = null;
        int hitCount = 0;
        try {
            InputStream urlStream = connectURL.openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlStream));
            GridBagConstraints cons = new GridBagConstraints();
            cons.anchor = GridBagConstraints.WEST;
            while ((line = reader.readLine()) != null) {
                if (line.indexOf("<FORM") != -1) {
                    readUCSCLocation(keyword, reader);
                    return;
                }
                if (line.indexOf("<H2>") != -1 || (hitCount == 500)) {
                    if (hitPanel != null) {
                        cons.gridx = 0;
                        cons.weighty = 100;
                        cons.gridy = hitCount;
                        cons.fill = GridBagConstraints.HORIZONTAL;
                        JLabel bttn = new JLabel("");
                        gbLayout.setConstraints(bttn, cons);
                        hitPanel.add(bttn);
                        tabs.add(head, new JScrollPane(hitPanel));
                    }
                    hitPanel = new JPanel();
                    gbLayout = new GridBagLayout();
                    hitPanel.setLayout(gbLayout);
                    hitPanel.setAlignmentY(0.0f);
                    if (line.indexOf("<H2>") != -1) {
                        head = line.substring(line.indexOf("H2>") + 3);
                        head = head.substring(0, head.indexOf("</H2>"));
                    } else {
                        head = "more";
                    }
                    hitCount = 0;
                }
                if (line.indexOf("HREF=") != -1) {
                    link = line.substring(line.indexOf("cgi-bin/") + 8, line.indexOf("\">"));
                    line = line.substring(line.indexOf("\">") + 2);
                    String linklabel = line.substring(0, line.indexOf("</A"));
                    content = line.substring(line.indexOf("</A>") + 4);
                    content = line.substring(line.indexOf("- ") + 2);
                    commentVector.add(content);
                    linkVector.add(linklabel);
                    hitCount++;
                }
            }
            if (hitPanel != null && hitCount != 0) {
                cons.gridx = 0;
                cons.weighty = 100;
                cons.weightx = 0;
                cons.gridy = hitCount;
                cons.fill = GridBagConstraints.HORIZONTAL;
                JLabel bttn = new JLabel("");
                gbLayout.setConstraints(bttn, cons);
                hitPanel.add(bttn);
                tabs.add(head, new JScrollPane(hitPanel));
            }
        } catch (Exception e) {
            System.out.println(">" + line + "<");
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "No Features Found for " + keyword);
            return;
        }
        final JList lst = new JList(linkVector);
        lst.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                int ind = lst.getSelectedIndex();
                commentText.setText((String) commentVector.elementAt(ind));
                commentText.select(0, 0);
            }
        });
        MouseListener mouseListener = new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = lst.locationToIndex(e.getPoint());
                    try {
                        String location = (String) lst.getModel().getElementAt(index), refStr, startStr, stopStr;
                        if (location.indexOf("at chr") != -1) {
                            location = location.substring(location.indexOf("at ") + 3);
                            refStr = location.substring(0, location.indexOf(":"));
                            location = location.substring(location.indexOf(":") + 1);
                            startStr = location.substring(0, location.indexOf("-"));
                            stopStr = location.substring(location.indexOf("-") + 1);
                            moveViewer(refStr, Integer.parseInt(startStr), Integer.parseInt(stopStr));
                        } else {
                            String hgsid = chooseHGVersion(selPanel.dsn);
                            URL connectURL = new URL("http://genome.ucsc.edu/cgi-bin/hgTracks?hgsid=" + hgsid + "&position=" + location);
                            InputStream urlStream = connectURL.openStream();
                            BufferedReader reader = new BufferedReader(new InputStreamReader(urlStream));
                            readUCSCLocation(location, reader);
                        }
                    } catch (Exception exc) {
                        exc.printStackTrace();
                    }
                }
            }
        };
        lst.addMouseListener(mouseListener);
        lst.setSelectedIndex(0);
        setTitle("Results for " + keyword);
        getContentPane().add(lst);
        scrollPane = new JScrollPane(commentText);
        JPanel pagePanel = new JPanel();
        final JButton prevBttn = new JButton("<=");
        final JButton nextBttn = new JButton("=>");
        prevBttn.setEnabled(page > 1);
        nextBttn.setEnabled(page + hitCount < matchingDocCount);
        ActionListener pageHandler = new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                if (ae.getSource() == prevBttn) {
                    System.out.println("prev!!!!");
                    doQuery(keyword);
                } else {
                    System.out.println("next!!!!");
                    doQuery(keyword);
                }
            }
        };
        pagePanel.add(prevBttn);
        prevBttn.addActionListener(pageHandler);
        pagePanel.add(nextBttn);
        nextBttn.addActionListener(pageHandler);
        JPanel hitsAndTextPanel = new JPanel();
        hitsAndTextPanel.setLayout(new GridLayout(2, 1));
        hitsAndTextPanel.add(new JScrollPane(lst));
        hitsAndTextPanel.add(scrollPane);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(hitsAndTextPanel, BorderLayout.CENTER);
        getContentPane().add(pagePanel, BorderLayout.SOUTH);
        setTitle("Results for " + keyword);
        setLocation((int) (owner.getLocation().getX()), (int) (owner.getLocation().getY() + owner.getSize().getHeight()));
        show();
    }
