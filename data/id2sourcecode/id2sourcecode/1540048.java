    private void createRulesPanel() {
        StyledTextPane textpane = new StyledTextPane();
        textpane.setText(getRules());
        NotesPanel scrollpane = new NotesPanel(textpane);
        scrollpane.setBorder(null);
        final int padding = 4, msgWidth = 500, msgHeight = 300;
        int x = padding, y = padding;
        scrollpane.setBounds(x, y, msgWidth, msgHeight);
        y += msgHeight + padding;
        JButton btn = GUIFactory.getButton_h2WithActivationBorder("OK");
        final JPanel me = this;
        btn.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                rulesPanel.setBounds(0, 0, 0, 0);
                Dimension size = actionPanel.getPreferredSize();
                int x = (me.getPreferredSize().width - size.width) / 2;
                int y = (me.getPreferredSize().height - size.height) / 2;
                actionPanel.setBounds(x, y, size.width, size.height);
            }
        });
        int btnWidth = GUIFactory.getStringWidth(btn, btn.getText());
        int btnHeight = GUIFactory.getStringHeight(btn, btn.getText());
        btnWidth += Globals.BUTTON_SIDE_PADDING;
        btnHeight += Globals.BUTTON_TOP_PADDING;
        x = padding + (msgWidth - btnWidth) / 2;
        btn.setBounds(x, y, btnWidth, btnHeight);
        final int panelWidth = padding + msgWidth + padding;
        final int panelHeight = padding + msgHeight + padding + btnHeight + padding;
        rulesPanel = new JPanel() {

            /** serial id. */
            private static final long serialVersionUID = 1L;

            /**
			 * {@inheritDoc}
			 */
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(panelWidth, panelHeight);
            }
        };
        rulesPanel.setLayout(null);
        rulesPanel.setOpaque(false);
        rulesPanel.setBounds(0, 0, 0, 0);
        rulesPanel.setBorder(new DialogBorder());
        rulesPanel.add(scrollpane);
        rulesPanel.add(btn);
        super.add(rulesPanel);
    }
