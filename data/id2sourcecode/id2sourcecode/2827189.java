    private void saveChatInFile(File temp, boolean formatted) {
        FileOutputStream fos = null;
        try {
            if (temp.exists()) {
                int n = JOptionPane.showConfirmDialog(this, "File Already Exist!, Overwrite it?", "Warning", JOptionPane.OK_CANCEL_OPTION);
                if (n != JOptionPane.OK_OPTION) {
                    return;
                }
            }
            temp.createNewFile();
            fos = new FileOutputStream(temp);
            if (formatted) {
                fos.write(jEditorPane1.getText().getBytes());
            } else {
                fos.write(jEditorPane1.getDocument().getText(0, jEditorPane1.getDocument().getLength()).getBytes());
            }
            JOptionPane.showMessageDialog(this, "Chat Log Saved Successfully!", "Chat Log Saving", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed To Save Chat Log!", "Error", JOptionPane.ERROR_MESSAGE, chatApp.getErrorIcon());
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
