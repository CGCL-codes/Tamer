    private User createUserFromPrompter() {
        JTextField nameField = new JTextField(15);
        JTextField passField = new JPasswordField(15);
        JTextField confirmField = new JPasswordField(15);
        JPanel namePanel = new JPanel(new BorderLayout());
        namePanel.add(new JLabel("User Name"), BorderLayout.WEST);
        namePanel.add(nameField, BorderLayout.EAST);
        JPanel passPanel = new JPanel(new BorderLayout());
        passPanel.add(new JLabel("Password"), BorderLayout.WEST);
        passPanel.add(passField, BorderLayout.EAST);
        JPanel confirmPanel = new JPanel(new BorderLayout());
        confirmPanel.add(new JLabel("Confirm Password"), BorderLayout.WEST);
        confirmPanel.add(confirmField, BorderLayout.EAST);
        Object[] messages = new Object[] { "Specify the User's Name and Password.", namePanel, passPanel, confirmPanel };
        String[] options = { "OK", "Cancel" };
        int option = JOptionPane.showOptionDialog(getPanel(), messages, "Specify the User's Name and Password", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        if (nameField.getText().equals("") || nameField.getText() == null || passField.getText().equals("") || passField.getText() == null) {
            return null;
        }
        if (!passField.getText().equals(confirmField.getText())) {
            JOptionPane.showMessageDialog(getPanel(), "The passwords you entered do not match.", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        User user = null;
        if (option == 0) {
            String password;
            try {
                password = new String(Hex.encodeHex(digester.digest(passField.getText().getBytes("UTF-8"))));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Unable to encode password", e);
            }
            user = new User(nameField.getText(), password);
        }
        return user;
    }
