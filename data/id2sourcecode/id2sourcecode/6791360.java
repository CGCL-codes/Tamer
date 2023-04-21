                public void actionPerformed(ActionEvent e) {
                    try {
                        if (type == 'A') {
                            viewuserdetail.dispose();
                            return;
                        }
                        if (ctrlTools.showYesNoQuestionMessage(viewuserdetail, ctrlXML.getInstance().getLanguageDataValue("all", "reallyapply")) == JOptionPane.YES_OPTION) {
                            if (txtID.getText().trim().length() == 0) {
                                ctrlTools.showInformationMessage(viewuserdetail, ctrlXML.getInstance().getLanguageDataValue("all", "missingvalue") + ctrlXML.getInstance().getLanguageDataValue(viewuserdetail.getClass().getSimpleName(), "lblid"));
                                txtID.requestFocus();
                                return;
                            }
                            if (txtName.getText().trim().length() == 0) {
                                ctrlTools.showInformationMessage(viewuserdetail, ctrlXML.getInstance().getLanguageDataValue("all", "missingvalue") + ctrlXML.getInstance().getLanguageDataValue(viewuserdetail.getClass().getSimpleName(), "lblname"));
                                txtName.requestFocus();
                                return;
                            }
                            if (new String(txtPWD.getPassword()).trim().length() == 0) {
                                ctrlTools.showInformationMessage(viewuserdetail, ctrlXML.getInstance().getLanguageDataValue("all", "missingvalue") + ctrlXML.getInstance().getLanguageDataValue(viewuserdetail.getClass().getSimpleName(), "lblpassword"));
                                txtPWD.requestFocus();
                                return;
                            }
                            MessageDigest md = MessageDigest.getInstance("MD5");
                            byte[] digest = md.digest(new String(txtPWD.getPassword()).getBytes());
                            String pwd = Base64.encode(digest);
                            if (type == 'N') {
                                if (ctrlDatabase.dcount(ctrlMain.getConnection(), "LAUSER", "UPPER(LAUID)='" + txtID.getText().trim().toUpperCase() + "'") > 0) {
                                    ctrlTools.showInformationMessage(viewuserdetail, ctrlXML.getInstance().getLanguageDataValue("all", "existingid"));
                                    txtID.requestFocus();
                                    return;
                                }
                                ctrlDatabase.executeQuery(ctrlMain.getConnection(), "INSERT INTO LASCS.LAUSER(LAUSTAT, LAUID, " + "LAUNAME, LAUFNAME, LAURIGHT, LAUPWD, LAUXNTZ) " + "VALUES('A', '" + txtID.getText() + "', '" + txtName.getText().trim() + "', '" + ((txtVName.getText() == null || txtVName.getText().trim().length() == 0) ? " " : txtVName.getText()) + "', " + cboRight.getSelectedItem().toString().substring(0, 1) + ", '" + pwd + "', '" + ctrlMain.getUser() + "')");
                            }
                            if (type == 'B') {
                                ctrlDatabase.executeQuery(ctrlMain.getConnection(), "UPDATE LASCS.LAUSER SET " + "LAUNAME='" + txtName.getText() + "', LAUFNAME='" + ((txtVName.getText() == null || txtVName.getText().trim().length() == 0) ? " " : txtVName.getText()) + "', LAURIGHT=" + cboRight.getSelectedItem().toString().substring(0, 1) + (isPasswordChanged ? ", LAUPWD='" + pwd + "'" : "") + ", LAUXNTZ='" + ctrlMain.getUser() + "', LAUXART='N' WHERE LAUID='" + txtID.getText().trim() + "'");
                            }
                            viewuserdetail.dispose();
                        }
                    } catch (Exception ex) {
                        ctrlTools.showErrorMessage(viewuserdetail, ex);
                    }
                }
