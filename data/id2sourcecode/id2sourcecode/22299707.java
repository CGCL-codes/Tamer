    private void createButtonActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            EnyarokClient client = EnyarokClient.get();
            client.connect(serverField.getText(), new Integer(portField.getText()));
            client.createAccount(nameField.getText(), new String(passwordField.getPassword()), eMailField.getText());
            client.login(nameField.getText(), new String(passwordField.getPassword()));
            CharacterFrame frame = CharacterFrame.get();
            StartFrame.get().setVisible(false);
            frame.setVisible(true);
            StartFrame.get().dispose();
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(StartFrame.get(), exception.toString(), "Error", JOptionPane.WARNING_MESSAGE);
        }
    }
