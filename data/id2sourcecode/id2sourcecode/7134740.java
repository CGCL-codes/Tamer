    private void updateBtnActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            String pass = new String(passTF.getPassword());
            StringBuffer requiredFields = new StringBuffer("");
            if (accessLevelCB.getSelectedIndex() < Util.FIRST_INDEX) {
                requiredFields.append(REQUIRED_FIELDS_ACCESS_LEVEL);
            }
            if (requiredFields.toString().equals("")) {
                if (validateCurrentPass()) {
                    if (!pass.equals("") && !validatePass()) {
                        JOptionPane.showMessageDialog(instance, MSG_INVALID_PASS, TITLE_WARNIG, JOptionPane.WARNING_MESSAGE);
                        return;
                    } else if (!pass.equals("") && validatePass()) {
                        byte[] b = CriptUtil.digest(new String(newPassTF.getPassword()).getBytes(), CriptUtil.SHA);
                        user.setPass(CriptUtil.byteArrayToHexString(b));
                    }
                } else {
                    JOptionPane.showMessageDialog(instance, MSG_INVALID_CURRENT_PASS, TITLE_WARNIG, JOptionPane.WARNING_MESSAGE);
                    return;
                }
                user.setAccessLevel(AccessLevel.get(accessLevelCB.getSelectedItem().toString()));
                facade.updateUser(user);
                JOptionPane.showMessageDialog(instance, MSG_SUCESS_UPDATE, TITLE_SUCESS, JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(instance, requiredFields.toString(), TITLE_REQUIRED_FIELDS, JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (OldVersionException ex) {
            Logger.getLogger(DetailUserPanel.class.getName()).log(Level.SEVERE, null, ex);
            if (Util.transactionErroDialog()) {
                try {
                    user = facade.loadUser(user.getId());
                    refresh();
                } catch (MyHibernateException ex1) {
                    Logger.getLogger(DetailUserPanel.class.getName()).log(Level.SEVERE, null, ex1);
                    Util.errorSQLPane(instance, ex1);
                }
            }
        } catch (MyHibernateException ex) {
            Logger.getLogger(DetailUserPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(DetailUserPanel.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }
