    public XALMagnetControlPanel(Electromagnet magnett) {
        super(magnett.getId());
        magnet = magnett;
        stat = false;
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        nrow = 1;
        ncolumn = 3;
        String[] hstr = { "channel", "read", "write" };
        String tag[] = { "BField" };
        Vector<Vector<String>> data = new Vector<Vector<String>>();
        for (int ir = 0; ir < nrow; ir++) {
            Vector<String> v = new Vector<String>();
            stat = true;
            for (int ic = 0; ic < ncolumn; ic++) {
                if (ic == 0) {
                    v.addElement(tag[ir]);
                } else {
                    v.addElement("");
                }
            }
            data.addElement(v);
        }
        Vector<String> header = new Vector<String>(Arrays.asList(hstr));
        table = new JTable(data, header);
        DefaultTableCellRenderer dtcr1 = new DefaultTableCellRenderer();
        dtcr1.setHorizontalAlignment(JLabel.RIGHT);
        DefaultTableCellRenderer dtcr2 = new DefaultTableCellRenderer();
        dtcr2.setBackground(Color.lightGray);
        dtcr2.setForeground(Color.blue);
        dtcr2.setHorizontalAlignment(JLabel.CENTER);
        JTextField tf = new JTextField("");
        tf.setEditable(false);
        DefaultCellEditor dce = new DefaultCellEditor(tf);
        table.setSize(20 + 30 + 30, 10);
        for (int ic = 0; ic < ncolumn; ic++) {
            if (ic == 0) {
                table.getColumnModel().getColumn(ic).setCellRenderer(dtcr2);
                table.getColumnModel().getColumn(ic).setCellEditor(dce);
                table.getColumnModel().getColumn(ic).setMinWidth(20);
            } else if (ic == 1) {
                table.getColumnModel().getColumn(ic).setCellRenderer(dtcr1);
                table.getColumnModel().getColumn(ic).setCellEditor(dce);
                table.getColumnModel().getColumn(ic).setMinWidth(30);
            } else {
                table.getColumnModel().getColumn(ic).setCellRenderer(dtcr1);
                table.getColumnModel().getColumn(ic).setMinWidth(30);
            }
        }
        JScrollPane sp = new JScrollPane();
        sp.setSize(new Dimension(150, 40));
        sp.getViewport().setView(table);
        sp.getViewport().setSize(new Dimension(150, 40));
        JPanel p = new JPanel();
        p.add(sp);
        getContentPane().add(p, BorderLayout.CENTER);
        connectChannel();
        readChannel();
        pack();
        setVisible(true);
        table.getModel().addTableModelListener(new TableModelListener() {

            public void tableChanged(TableModelEvent e) {
                if (e.getColumn() == 2) {
                    System.out.println("column =" + e.getColumn());
                    System.out.println("firstr, lastr =" + e.getFirstRow() + " " + e.getLastRow());
                    writeChannel();
                }
            }
        });
    }
