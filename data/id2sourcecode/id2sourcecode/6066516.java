    void createUserButton_actionPerformed(ActionEvent e) {
        String permissions = "(read)(write)(view)(delete)(resume)(rename)(makedir)(deletedir)(real_quota)";
        try {
            String username = writeUser(permissions);
            if (username != null) {
                NMCommon.recurseCopy("/Library/Application Support/NotMac/templateUserHome/", baseNotMacText.getText() + username + "/");
                JOptionPane.showMessageDialog(this, "User created.");
            }
        } catch (Exception ee) {
            ee.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error:" + ee.toString());
        }
    }
