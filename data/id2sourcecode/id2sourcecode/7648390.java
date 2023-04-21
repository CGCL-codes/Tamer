    @Override
    public void actionPerformed(ActionEvent arg0) {
        LogicFactory logicfactory = null;
        System.err.print("Anmelden: ");
        if (arg0.getSource() == cancelbutton) {
            System.exit(0);
        }
        try {
            logicfactory = login.authenticate(userfield.getText(), Digest.digest(new String(passwordfield.getPassword()).getBytes()));
        } catch (AuthenticationException e) {
            passwordfield.setText("");
            JOptionPane.showMessageDialog(this, "BenutzerInnenname und/oder Passwort falsch!", "Fehler", JOptionPane.ERROR_MESSAGE);
            System.err.println("Wrong password");
            return;
        } catch (NoSuchAlgorithmException e) {
            JOptionPane.showMessageDialog(this, "Kann Passwort nicht verschlüsseln. MAS-C wird beendet.", "Kritischer Fehler", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        System.err.println("[OK]");
        this.setVisible(false);
        this.dispose();
        Init.initGUI(logicfactory.getMeetingLogic(), logicfactory.getUserLogic());
    }
