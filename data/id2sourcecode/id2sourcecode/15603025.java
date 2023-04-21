    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        new Thread() {

            public void run() {
                jProgressBar1.setIndeterminate(true);
                jTextField1.setEnabled(false);
                jPasswordField1.setEnabled(false);
                jButton1.setEnabled(false);
                jComboBox1.setEnabled(false);
                jComboBox2.setEnabled(false);
                try {
                    boolean con = handler.connect(jComboBox1.getSelectedItem().toString(), Integer.parseInt(jComboBox2.getSelectedItem().toString()));
                    if (con && 0 != handler.login(jTextField1.getText(), new String(jPasswordField1.getPassword()))) {
                        try {
                            handler.users();
                        } catch (LoginException e) {
                            try {
                                handler.bye();
                            } catch (IOException e1) {
                            } finally {
                                handler.close();
                            }
                        }
                        getParent().setVisible(true);
                        setVisible(false);
                    } else {
                        jProgressBar1.setString("Wrong name or bad password.");
                    }
                } catch (IOException e) {
                    jProgressBar1.setString("Network Error.");
                    e.printStackTrace();
                } finally {
                    jProgressBar1.setIndeterminate(false);
                    jTextField1.setEnabled(true);
                    jPasswordField1.setEnabled(true);
                    jButton1.setEnabled(true);
                    jComboBox1.setEnabled(true);
                    jComboBox2.setEnabled(true);
                }
            }
        }.start();
    }
