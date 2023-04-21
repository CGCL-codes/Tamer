    public DocumentPropertiesDialog(JFrame frame) {
        super(frame, true);
        setResizable(false);
        setSize(SIZE);
        formPanel = new QPanel(new FormLayout(5, 3));
        formPanel.setBorder(new CompoundBorder(new LineBorder(Color.darkGray), new EmptyBorder(5, 5, 5, 5)));
        formPanel.setGradientBackground(true);
        nameField = new JTextField();
        formPanel.add(new JLabel("Name:"), FormLayout.LEFT);
        formPanel.add(nameField, FormLayout.RIGHT_FILL);
        validateField = new JCheckBox("Validate");
        validateField.setBorder(null);
        formPanel.add(validateField, FormLayout.RIGHT);
        JPanel separator1 = new JPanel();
        separator1.setPreferredSize(new Dimension(100, 10));
        separator1.setOpaque(false);
        formPanel.add(separator1, FormLayout.FULL);
        locationField = new JTextField();
        locationField.setBorder(null);
        locationField.setEditable(false);
        locationField.setOpaque(false);
        formPanel.add(new JLabel("Location:"), FormLayout.LEFT);
        formPanel.add(locationField, FormLayout.RIGHT_FILL);
        modifiedField = new JTextField();
        modifiedField.setBorder(null);
        modifiedField.setEditable(false);
        modifiedField.setOpaque(false);
        formPanel.add(new JLabel("Modified:"), FormLayout.LEFT);
        formPanel.add(modifiedField, FormLayout.RIGHT_FILL);
        sizeField = new JTextField();
        sizeField.setBorder(null);
        sizeField.setEditable(false);
        sizeField.setOpaque(false);
        formPanel.add(new JLabel("Size:"), FormLayout.LEFT);
        formPanel.add(sizeField, FormLayout.RIGHT_FILL);
        JPanel separator2 = new JPanel();
        separator2.setPreferredSize(new Dimension(100, 5));
        separator2.setOpaque(false);
        formPanel.add(separator2, FormLayout.FULL);
        formPanel.add(new JLabel("Attributes:"), FormLayout.LEFT);
        readBox = new JLabel("Read");
        readBox.setFont(readBox.getFont().deriveFont(Font.PLAIN));
        readBox.setForeground(Color.black);
        readBox.setOpaque(false);
        writeBox = new JLabel("Write");
        writeBox.setFont(readBox.getFont());
        writeBox.setForeground(Color.black);
        writeBox.setOpaque(false);
        hiddenBox = new JLabel("Hidden");
        hiddenBox.setFont(readBox.getFont());
        hiddenBox.setForeground(Color.black);
        hiddenBox.setOpaque(false);
        JPanel attributesPanel = new JPanel(new GridLayout(0, 3));
        attributesPanel.setOpaque(false);
        attributesPanel.add(readBox);
        attributesPanel.add(writeBox);
        attributesPanel.add(hiddenBox);
        formPanel.add(attributesPanel, FormLayout.RIGHT_FILL);
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        buttonPanel.setBorder(new EmptyBorder(5, 0, 3, 0));
        cancelButton = new JButton("Cancel");
        cancelButton.setFont(cancelButton.getFont().deriveFont(Font.PLAIN));
        cancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                cancelButtonPressed();
            }
        });
        okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                okButtonPressed();
            }
        });
        getRootPane().setDefaultButton(okButton);
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(2, 2, 2, 2));
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        setContentPane(panel);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
    }
