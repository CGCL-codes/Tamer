    private void buildGui() {
        frame = new JFrame("Message generator");
        frame.setSize(900, 575);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        headlineLabel = new JLabel("Properties:");
        headlineLabel.setBounds(coordXLabel, 50, 150, height);
        headlineLabel.setFont(new Font("Serif", Font.ITALIC, height));
        channelLabel = new JLabel("Channel");
        channelLabel.setBounds(coordXLabel, coordY, widthLabel, height);
        channelBox = new JComboBox(data.getChannels());
        channelBox.setBounds(coordXComboBox, coordY, widthComboBox, height);
        patientLabel = new JLabel("Patient");
        patientLabel.setBounds(coordXLabel, coordY + 50, widthLabel, height);
        patientBox = new JComboBox(data.getPatients());
        patientBox.setBounds(coordXComboBox, coordY + 50, widthComboBox, height);
        locationLabel = new JLabel("Location");
        locationLabel.setBounds(coordXLabel, coordY + 100, widthLabel, height);
        locationBox = new JComboBox(data.getLocations());
        locationBox.setBounds(coordXComboBox, coordY + 100, widthComboBox, height);
        deviceLabel = new JLabel("Device");
        deviceLabel.setBounds(coordXLabel, coordY + 150, widthLabel, height);
        deviceBox = new JComboBox(data.getDevices());
        deviceBox.setBounds(coordXComboBox, coordY + 150, widthComboBox, height);
        staffLabel = new JLabel("Staff");
        staffLabel.setBounds(coordXLabel, coordY + 200, widthLabel, height);
        staffBox = new JComboBox(data.getStaff());
        staffBox.setBounds(coordXComboBox, coordY + 200, widthComboBox, height);
        staffNameLabel = new JLabel("Name Staff");
        staffNameLabel.setBounds(coordXLabel, coordY + 250, widthLabel, height);
        staffNameBox = new JComboBox(data.getStaffNames());
        staffNameBox.setBounds(coordXComboBox, coordY + 250, widthComboBox, height);
        infoLabel = new JLabel("Information");
        infoLabel.setBounds(coordXLabel, coordY + 300, widthLabel, height);
        infoBox = new JComboBox(data.getInformation());
        infoBox.setBounds(coordXComboBox, coordY + 300, widthComboBox, height);
        btn1 = new JButton(">>");
        btn1.setBounds(300, 200, 75, height);
        btn1.addActionListener(new btn1ActionListener());
        btn2 = new JButton("<<");
        btn2.setBounds(300, 275, 75, height);
        btn2.addActionListener(new btn2ActionListener());
        btn3 = new JButton("Done");
        btn3.setBounds(75, coordY + 350, 75, height);
        btn3.addActionListener(new btn3ActionListener());
        btn4 = new JButton("Clear");
        btn4.setBounds(200, coordY + 350, 75, height);
        btn4.addActionListener(new btn4ActionListener());
        model = new DefaultTableModel(columnNames, 0);
        class MyTableCellRenderer extends JTextArea implements TableCellRenderer {

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (isSelected) {
                    setForeground(table.getSelectionForeground());
                    setBackground(table.getSelectionBackground());
                } else {
                    setForeground(table.getForeground());
                    setBackground(table.getBackground());
                }
                setWrapStyleWord(true);
                setLineWrap(true);
                setText(value.toString());
                return this;
            }
        }
        table = new JTable(model);
        table.setSize(400, 400);
        table.setRowHeight(180);
        table.setDefaultRenderer(Object.class, new MyTableCellRenderer());
        scrollPane = new JScrollPane(table);
        scrollPane.setBounds(425, 50, 400, 450);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        container = new Container();
        container.add(btn1);
        container.add(btn2);
        container.add(btn3);
        container.add(btn4);
        container.add(patientBox);
        container.add(patientLabel);
        container.add(channelBox);
        container.add(channelLabel);
        container.add(locationBox);
        container.add(locationLabel);
        container.add(deviceBox);
        container.add(deviceLabel);
        container.add(staffBox);
        container.add(staffLabel);
        container.add(infoBox);
        container.add(infoLabel);
        container.add(scrollPane);
        container.add(headlineLabel);
        container.add(staffNameLabel);
        container.add(staffNameBox);
        frame.setContentPane(container);
        frame.setVisible(false);
    }
