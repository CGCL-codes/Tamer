    @Override
    public void actionPerformed(ActionEvent e) {
        System.err.println("button clicked: " + e.getSource());
        if (e.getSource() == cancelbutton) {
            this.setVisible(false);
            this.dispose();
            return;
        }
        if (!email.getText().equals(userlogic.getMailAddress())) {
            userlogic.setEmail(email.getText());
        }
        try {
            if (oldpw.getPassword().length != 0) {
                if (newpw.getPassword().length != 0 && newpw2.getPassword().length != 0) {
                    if (Arrays.equals(newpw.getPassword(), newpw2.getPassword())) {
                        String oldPassword = Digest.digest(new String(oldpw.getPassword()).getBytes());
                        String newPassword = Digest.digest(new String(newpw.getPassword()).getBytes());
                        if (userlogic.setPassword(oldPassword, newPassword)) {
                            JOptionPane.showMessageDialog(this, "Das Passwort wurde erfolgreich geändert.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Die Passwörter stimmen nicht überein.", "Fehler", JOptionPane.ERROR_MESSAGE);
                        newpw.setText("");
                        newpw2.setText("");
                        return;
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Das neue Passwort darf nicht leer sein.", "Fehler", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        } catch (NoSuchAlgorithmException e1) {
            JOptionPane.showMessageDialog(this, "Das Passwort konnte nicht geändert werden.", "Fehler", JOptionPane.ERROR_MESSAGE);
        }
        this.setVisible(false);
        this.dispose();
    }
