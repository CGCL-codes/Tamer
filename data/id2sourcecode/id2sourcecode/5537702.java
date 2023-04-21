            public void actionPerformed(ActionEvent ev) {
                boolean do_transfer = false;
                try {
                    String expname = getNickname();
                    int split = expname.lastIndexOf('.');
                    String domain = "";
                    String name = "";
                    String usersdomain = Prefs.getConfigValue("default", "domainname");
                    if (split > 0) {
                        domain = expname.substring(0, split);
                        name = expname.substring(split + 1, expname.length());
                    } else {
                        name = expname;
                    }
                    name = name.trim();
                    if (name.equals("")) {
                        JOptionPane.showMessageDialog(null, "Cowardly refusing to upload with an empty buffer name...");
                        return;
                    }
                    if (!domain.equals(usersdomain)) {
                        int s = JOptionPane.showConfirmDialog(null, "If you are not the original author, you may wish to switch the current domain name " + domain + " to \nyour domain name " + usersdomain + ".  Would you like to do this?\n (If you'll be using this domain often, you may want to set it in your preferences.)", "Potential WWW name-space clash!", JOptionPane.YES_NO_CANCEL_OPTION);
                        if (s == JOptionPane.YES_OPTION) {
                            setNickname(usersdomain + "." + name);
                            do_transfer = executeTransfer();
                        }
                        if (s == JOptionPane.NO_OPTION) {
                            do_transfer = executeTransfer();
                        }
                    } else {
                        do_transfer = executeTransfer();
                    }
                } catch (Exception err) {
                    throw new RuntimeException("Problem uploading storage.", err);
                }
                if (do_transfer) {
                    int s = JOptionPane.showConfirmDialog(null, "At this time you may also upload the individual flat file storage specifications supporting this set.\nWould you like to do this? ", "Create/Upload Flat File Specs Too?", JOptionPane.YES_NO_CANCEL_OPTION);
                    if (s == JOptionPane.YES_OPTION) {
                        boolean yes_to_all = false;
                        for (int m = 0; m < sourcemodel.getRowCount(); m++) {
                            FlatFileDOM dom = sourcemodel.getFlatFileDOM(m);
                            String nn = dom.getNickname();
                            if (nn != null) {
                                nn = nn.trim();
                            }
                            if ((nn != null) && (nn.length() != 0)) {
                                if (Connection.storageExists(nn)) {
                                    int t = -1;
                                    if (!yes_to_all) {
                                        Object[] options = { "Yes", "No", "Yes to all" };
                                        t = JOptionPane.showOptionDialog(FlatFileSetFrame.this, "Storage " + nn + " already exists.  Would you like to overwrite?", "Overwrite Flat File Spec?", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
                                    } else {
                                        t = 0;
                                    }
                                    if (t == 2) {
                                        yes_to_all = true;
                                        t = 0;
                                    }
                                    if (t == 0) {
                                        System.out.println("Overwriting existing storage " + nn);
                                        FlatFileStorage target = (FlatFileStorage) Connection.getStorage(nn);
                                        if (target == null) {
                                            throw new RuntimeException("Storage " + nn + " was indicated to exist but could not be retrieved.");
                                        }
                                        target.transferStorage(dom);
                                    }
                                } else {
                                    Connection.createStorage(FlatFileStorage.class, nn);
                                    FlatFileStorage target = (FlatFileStorage) Connection.getStorage(nn);
                                    target.transferStorage(dom);
                                }
                            }
                        }
                    }
                }
                setEditable(true);
            }
