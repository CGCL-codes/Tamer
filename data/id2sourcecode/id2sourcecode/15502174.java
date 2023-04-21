    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
        JPanel panel_1 = new JPanel();
        tabbedPane.addTab("New tab", null, panel_1, null);
        GridBagLayout gbl_panel_1 = new GridBagLayout();
        gbl_panel_1.columnWidths = new int[] { 437, 0 };
        gbl_panel_1.rowHeights = new int[] { 124, 120, 0 };
        gbl_panel_1.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
        gbl_panel_1.rowWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
        panel_1.setLayout(gbl_panel_1);
        JScrollPane scrollPane = new JScrollPane();
        textPane = new JTextPane();
        textPane.setEditable(false);
        scrollPane.setViewportView(textPane);
        GridBagConstraints gbc_scrollPane = new GridBagConstraints();
        gbc_scrollPane.fill = GridBagConstraints.BOTH;
        gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
        gbc_scrollPane.gridx = 0;
        gbc_scrollPane.gridy = 0;
        panel_1.add(scrollPane, gbc_scrollPane);
        JPanel panel = new JPanel();
        FlowLayout flowLayout = (FlowLayout) panel.getLayout();
        flowLayout.setAlignment(FlowLayout.LEFT);
        GridBagConstraints gbc_panel = new GridBagConstraints();
        gbc_panel.fill = GridBagConstraints.BOTH;
        gbc_panel.gridx = 0;
        gbc_panel.gridy = 1;
        panel_1.add(panel, gbc_panel);
        JButton btnNewButton = new JButton("Set output");
        btnNewButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                defaultOut = System.out;
                OutputStream out = new OutputStream() {

                    private StringBuilder sb = new StringBuilder();

                    private String endLineMark = System.getProperty("line.separator");

                    private int endLineMarkLength = endLineMark.length();

                    private char[] buff = new char[endLineMarkLength];

                    @Override
                    public void write(int b) throws IOException {
                        sb.append((char) b);
                        for (int i = 0; i < buff.length - 1; i++) {
                            buff[i] = buff[i + 1];
                        }
                        buff[buff.length - 1] = (char) b;
                        String tmp = String.valueOf(buff);
                        if (tmp.equals(endLineMark)) {
                            textPane.setText(sb.toString());
                        }
                    }
                };
                PrintStream newOut = new PrintStream(out);
                System.setOut(newOut);
            }
        });
        panel.add(btnNewButton);
        JButton btnNewButton_1 = new JButton("Test 2");
        btnNewButton_1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.out.println("abc");
            }
        });
        panel.add(btnNewButton_1);
        JButton btnNewButton_2 = new JButton("clear");
        btnNewButton_2.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                textPane.setText("");
            }
        });
        panel.add(btnNewButton_2);
        JButton btnNewButton_3 = new JButton("Set output 2");
        btnNewButton_3.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.setOut(defaultOut);
            }
        });
        panel.add(btnNewButton_3);
        JButton btnNewButton_6 = new JButton("Test1");
        btnNewButton_6.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.out.print("shitttt");
            }
        });
        panel.add(btnNewButton_6);
        JButton btnNewButton_5 = new JButton("Test 3");
        btnNewButton_5.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String str = textPane.getText();
                char[] arr = str.toCharArray();
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < arr.length; i++) {
                    sb.append("[");
                    sb.append((int) arr[i]);
                    sb.append("]");
                }
                textPane.setText(sb.toString());
            }
        });
        panel.add(btnNewButton_5);
        JButton btnNewButton_4 = new JButton("New button");
        panel.add(btnNewButton_4);
        JButton btnNewButton_7 = new JButton("New button");
        panel.add(btnNewButton_7);
        JButton btnNewButton_8 = new JButton("New button");
        panel.add(btnNewButton_8);
        JButton btnNewButton_9 = new JButton("New button");
        panel.add(btnNewButton_9);
        JButton btnNewButton_10 = new JButton("New button");
        panel.add(btnNewButton_10);
        JButton btnNewButton_11 = new JButton("New button");
        panel.add(btnNewButton_11);
    }
