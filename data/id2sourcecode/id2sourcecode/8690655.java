    private void bt_okActionPerformed(java.awt.event.ActionEvent evt) {
        bt_ok.requestFocus();
        dbName = "";
        Boolean loadDemoData = false;
        if (combobox_databases.getSelectedIndex() > 0) {
            dbName = (String) combobox_databases.getSelectedItem();
            loadDemoData = false;
        } else {
            dbName = textbox_name.getText().trim();
            loadDemoData = false;
            createNewDB = true;
        }
        if (new RequiredValidator(dbName).validate()) {
            try {
                if (!dbName.endsWith(".db")) {
                    dbName += ".db";
                }
                if (createNewDB) {
                    MySeriesLogger.logger.log(Level.INFO, "Create new DB");
                    CreateDatabase d = new CreateDatabase(this, dbName);
                    Thread t = new Thread(d);
                    t.start();
                    dispose();
                } else {
                    DBConnection conn = new DBConnection(dbName, false);
                    if (DBConnection.isConnected) {
                        MySeriesLogger.logger.log(Level.INFO, "Check database format");
                        if (DBConnection.checkDatabase()) {
                            if (DBConnection.isConnected) {
                                MySeriesLogger.logger.log(Level.INFO, "MySerieS restarting");
                                dispose();
                                MySeries m = new MySeries();
                            } else {
                                dispose();
                                MyMessages.error("Invalid Database", "Could not connect to the selected database.\nPlease select another one or create a new one.", true);
                                StartPanel s = new StartPanel();
                            }
                        } else {
                            dispose();
                            MyMessages.error("Invalid Database", "The selected database seems to be invalid.\nPlease select another one or create a new one.", true);
                            StartPanel s = new StartPanel();
                        }
                    }
                }
            } catch (ClassNotFoundException ex) {
                MySeriesLogger.logger.log(Level.SEVERE, "Database library not found", ex);
            } catch (SQLException ex) {
                MySeriesLogger.logger.log(Level.SEVERE, "An sql exception occured", ex);
            } catch (IOException ex) {
                MySeriesLogger.logger.log(Level.SEVERE, "Could not read/write to database", ex);
            } catch (InstantiationException ex) {
                MySeriesLogger.logger.log(Level.SEVERE, null, ex);
                MyMessages.error("My Series", "Could not create Application", true);
            } catch (IllegalAccessException ex) {
                MySeriesLogger.logger.log(Level.SEVERE, null, ex);
                MyMessages.error("My Series", "Illegal access", true);
            } catch (UnsupportedLookAndFeelException ex) {
                MySeriesLogger.logger.log(Level.SEVERE, null, ex);
                MyMessages.error("My Series", "Look and feel is not supported", true);
            } catch (Exception ex) {
                MyMessages.error("My Series", ex.getMessage(), true);
                MySeriesLogger.logger.log(Level.SEVERE, ex.getMessage(), ex);
            }
        } else {
            MySeriesLogger.logger.log(Level.WARNING, "The database name should not be empty");
            MyMessages.validationError("Empty name", "The database name should not be empty");
        }
    }
