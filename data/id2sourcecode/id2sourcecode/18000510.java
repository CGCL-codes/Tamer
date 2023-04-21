    private void doGenerateKeypair() {
        if (keygenFrame == null) {
            keygenFrame = new KeygenFrame(keymanager, labels);
            keygenFrame.setLocationRelativeTo(null);
        }
        if (keygenFrame.showModal(localUserName.getText())) {
            HexUtils.addEntryTo(TraserConstants.DEFAULT_PASSWORD_FILE, localUserName.getText(), keygenFrame.getPassword());
            JOptionPane.showMessageDialog(null, labels.get("configtool_genkey_success"), getTitle(), JOptionPane.INFORMATION_MESSAGE);
        }
    }
