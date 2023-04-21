    private void loginButtonActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            Client client = Client.get();
            client.connect(serverField.getText(), new Integer(portField.getText()));
            client.login(nameField.getText(), new String(passwordField.getPassword()));
            client.chooseCharacter(nameField.getText());
            ChatFrame chat = ChatFrame.get();
            chat.setVisible(true);
            MainScreen.get().setVisible(false);
            MainScreen.get().dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(MainScreen.get(), e.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
            e.printStackTrace();
        }
    }
