    private void createButtonActionPerformed(java.awt.event.ActionEvent evt) {
        Client client = Client.get();
        final boolean ok = checkFields();
        if (!ok) {
            return;
        }
        try {
            client.connect(serverField.getText(), new Integer(portField.getText()));
            client.createAccount(nameField.getText(), new String(password1Field.getPassword()), mailField.getText());
            client.login(nameField.getText(), new String(password1Field.getPassword()));
            RPObject character = new RPObject();
            client.createCharacter(nameField.getText(), character);
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
