    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jLabel = new JLabel();
            jLabel.setBounds(new Rectangle(90, 15, 91, 76));
            jLabel.setText(String.valueOf(controller.getChannel(0)));
            jContentPane = new JPanel();
            jContentPane.setLayout(null);
            jContentPane.add(getJButton(), null);
            jContentPane.add(getJButton1(), null);
            jContentPane.add(jLabel, null);
        }
        return jContentPane;
    }
