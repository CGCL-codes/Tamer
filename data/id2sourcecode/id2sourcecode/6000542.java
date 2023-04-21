    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cancelbutton) {
            this.setVisible(false);
            this.dispose();
            return;
        }
        try {
            if (!(username.getText().length() == 0 || forename.getText().length() == 0 || surname.getText().length() == 0)) {
                if (newpw.getPassword().length != 0 && newpw2.getPassword().length != 0) {
                    if (Arrays.equals(newpw.getPassword(), newpw2.getPassword())) {
                        String password = Digest.digest(new String(newpw.getPassword()).getBytes());
                        if (!userlogic.addUser(username.getText(), forename.getText(), surname.getText(), password, email.getText())) {
                            JOptionPane.showMessageDialog(this, "BenutzerIn konnte nicht angelegt werden. Bitte versuchen sie es mit einem anderen BenutzerInnennamen erneut.");
                            return;
                        }
                        JOptionPane.showMessageDialog(this, "NeueR BenutzerIn angelegt. Sie können sich jetzt mit der eingegebenen Kombination aus BenutzerInnnenname und Passwort anmelden.");
                    } else {
                        JOptionPane.showMessageDialog(this, "Die Passwörter stimmen nicht überein.", "Fehler", JOptionPane.ERROR_MESSAGE);
                        newpw.setText("");
                        newpw2.setText("");
                        return;
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Das Passwort darf nicht leer sein.", "Fehler", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else {
                JOptionPane.showMessageDialog(this, "Die Felder BenutzerInnenname, Vorname und Nachname müssen ausgefüllt sein.", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NoSuchAlgorithmException e1) {
            JOptionPane.showMessageDialog(this, "Konnte keineN neueN BenutzerIn anlegen.", "Fehler", JOptionPane.ERROR_MESSAGE);
        }
        this.setVisible(false);
        this.dispose();
    }
