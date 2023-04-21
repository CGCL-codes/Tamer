    private void loadKeypair() {
        File keyFile = new File(frame1.keypool + board + ".key");
        privateKeyTextField.setText(LangRes.getString("Not available"));
        publicKeyTextField.setText(LangRes.getString("Not available"));
        if (keyFile.isFile() && keyFile.length() > 0) {
            String privateKey = SettingsFun.getValue(keyFile, "privateKey");
            String publicKey = SettingsFun.getValue(keyFile, "publicKey");
            String state = SettingsFun.getValue(keyFile, "state");
            if (!privateKey.equals("")) privateKeyTextField.setText(privateKey);
            if (!publicKey.equals("")) publicKeyTextField.setText(publicKey);
            if (state.equals("writeAccess") || state.equals("readAccess")) {
                privateKeyTextField.setEnabled(true);
                publicKeyTextField.setEnabled(true);
                generateKeyButton.setEnabled(true);
                secureBoardRadioButton.setSelected(true);
            } else {
                privateKeyTextField.setEnabled(false);
                publicKeyTextField.setEnabled(false);
                generateKeyButton.setEnabled(false);
                publicBoardRadioButton.setSelected(true);
            }
        }
    }
