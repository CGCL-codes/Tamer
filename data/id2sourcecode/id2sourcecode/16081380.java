    public void actionPerformed(ActionEvent ev) {
        String s = ev.getActionCommand();
        if (s == null) {
            if (ev.getSource() instanceof JMenuItem) {
                s = ((JMenuItem) ev.getSource()).getText();
            }
        }
        if (s == null) {
        } else if (s.equals("Exit")) {
            windowClosing(null);
        } else if (s.equals("Transfer")) {
            Transfer.work(null);
        } else if (s.equals("Dump")) {
            Transfer.work(new String[] { "-d" });
        } else if (s.equals("Restore")) {
            JOptionPane.showMessageDialog(fMain.getContentPane(), "Use Ctrl-R or the View menu to\n" + "update nav. tree after Restoration", "Suggestion", JOptionPane.INFORMATION_MESSAGE);
            Transfer.work(new String[] { "-r" });
        } else if (s.equals(LOGGING_BOX_TEXT)) {
            JavaSystem.setLogToSystem(boxLogging.isSelected());
        } else if (s.equals(AUTOREFRESH_BOX_TEXT)) {
            autoRefresh = boxAutoRefresh.isSelected();
            refreshTree();
        } else if (s.equals("Refresh Tree")) {
            refreshTree();
        } else if (s.startsWith("#")) {
            int i = Integer.parseInt(s.substring(1));
            txtCommand.setText(sRecent[i]);
        } else if (s.equals("Connect...")) {
            Connection newCon = null;
            try {
                setWaiting("Connecting");
                newCon = ConnectionDialogSwing.createConnection(jframe, "Connect");
            } finally {
                setWaiting(null);
            }
            connect(newCon);
        } else if (s.equals(GRID_BOX_TEXT)) {
            gridFormat = boxShowGrid.isSelected();
            displayResults();
        } else if (s.equals("Open Script...")) {
            JFileChooser f = new JFileChooser(".");
            f.setDialogTitle("Open Script...");
            if (defDirectory != null) {
                f.setCurrentDirectory(new File(defDirectory));
            }
            int option = f.showOpenDialog((Component) fMain);
            if (option == JFileChooser.APPROVE_OPTION) {
                File file = f.getSelectedFile();
                if (file != null) {
                    sqlScriptBuffer = DatabaseManagerCommon.readFile(file.getAbsolutePath());
                    if (4096 <= sqlScriptBuffer.length()) {
                        int eoThirdLine = sqlScriptBuffer.indexOf('\n');
                        if (eoThirdLine > 0) {
                            eoThirdLine = sqlScriptBuffer.indexOf('\n', eoThirdLine + 1);
                        }
                        if (eoThirdLine > 0) {
                            eoThirdLine = sqlScriptBuffer.indexOf('\n', eoThirdLine + 1);
                        }
                        if (eoThirdLine < 1) {
                            eoThirdLine = 100;
                        }
                        txtCommand.setText("............... Script File loaded: " + file + " ..................... \n" + "............... Click Execute or Clear " + "...................\n" + sqlScriptBuffer.substring(0, eoThirdLine + 1) + "........................................." + "................................\n" + "..........................................." + "..............................\n");
                        txtCommand.setEnabled(false);
                    } else {
                        txtCommand.setText(sqlScriptBuffer);
                        sqlScriptBuffer = null;
                        txtCommand.setEnabled(true);
                    }
                }
            }
        } else if (s.equals("Save Script...")) {
            JFileChooser f = new JFileChooser(".");
            f.setDialogTitle("Save Script");
            if (defDirectory != null) {
                f.setCurrentDirectory(new File(defDirectory));
            }
            int option = f.showSaveDialog((Component) fMain);
            if (option == JFileChooser.APPROVE_OPTION) {
                File file = f.getSelectedFile();
                if (file != null) {
                    DatabaseManagerCommon.writeFile(file.getAbsolutePath(), txtCommand.getText());
                }
            }
        } else if (s.equals("Save Result...")) {
            JFileChooser f = new JFileChooser(".");
            f.setDialogTitle("Save Result...");
            if (defDirectory != null) {
                f.setCurrentDirectory(new File(defDirectory));
            }
            int option = f.showSaveDialog((Component) fMain);
            if (option == JFileChooser.APPROVE_OPTION) {
                File file = f.getSelectedFile();
                if (file != null) {
                    showResultInText();
                    DatabaseManagerCommon.writeFile(file.getAbsolutePath(), txtResult.getText());
                }
            }
        } else if (s.equals(SHOWSYS_BOX_TEXT)) {
            showSys = boxShowSys.isSelected();
            refreshTree();
        } else if (s.equals(ROWCOUNTS_BOX_TEXT)) {
            displayRowCounts = boxRowCounts.isSelected();
            refreshTree();
        } else if (s.startsWith("LFMODE:")) {
            setLF(s.substring("LFMODE:".length()));
        } else if (s.equals("Set Fonts")) {
            FontDialogSwing.CreatFontDialog(refForFontDialogSwing);
        } else if (s.equals(AUTOCOMMIT_BOX_TEXT)) {
            try {
                cConn.setAutoCommit(boxAutoCommit.isSelected());
            } catch (SQLException e) {
                boxAutoCommit.setSelected(!boxAutoCommit.isSelected());
                CommonSwing.errorMessage(e);
            }
        } else if (s.equals("COMMIT*")) {
            try {
                cConn.commit();
                showHelp(new String[] { "", "COMMIT executed" });
            } catch (SQLException e) {
                CommonSwing.errorMessage(e);
            }
        } else if (s.equals("Insert test data")) {
            insertTestData();
            refreshTree();
        } else if (s.equals("ROLLBACK*")) {
            try {
                cConn.rollback();
                showHelp(new String[] { "", "ROLLBACK executed" });
            } catch (SQLException e) {
                CommonSwing.errorMessage(e);
            }
        } else if (s.equals("Disable MaxRows")) {
            try {
                sStatement.setMaxRows(0);
            } catch (SQLException e) {
                CommonSwing.errorMessage(e);
            }
        } else if (s.equals("Set MaxRows to 100")) {
            try {
                sStatement.setMaxRows(100);
            } catch (SQLException e) {
                CommonSwing.errorMessage(e);
            }
        } else if (s.equals("SELECT")) {
            showHelp(DatabaseManagerCommon.selectHelp);
        } else if (s.equals("INSERT")) {
            showHelp(DatabaseManagerCommon.insertHelp);
        } else if (s.equals("UPDATE")) {
            showHelp(DatabaseManagerCommon.updateHelp);
        } else if (s.equals("DELETE")) {
            showHelp(DatabaseManagerCommon.deleteHelp);
        } else if (s.equals("EXECUTE")) {
            executeCurrentSQL();
        } else if (s.equals("CREATE TABLE")) {
            showHelp(DatabaseManagerCommon.createTableHelp);
        } else if (s.equals("DROP TABLE")) {
            showHelp(DatabaseManagerCommon.dropTableHelp);
        } else if (s.equals("CREATE INDEX")) {
            showHelp(DatabaseManagerCommon.createIndexHelp);
        } else if (s.equals("DROP INDEX")) {
            showHelp(DatabaseManagerCommon.dropIndexHelp);
        } else if (s.equals("CHECKPOINT*")) {
            try {
                cConn.createStatement().executeUpdate("CHECKPOINT");
                showHelp(new String[] { "", "CHECKPOINT executed" });
            } catch (SQLException e) {
                CommonSwing.errorMessage(e);
            }
        } else if (s.equals("SCRIPT")) {
            showHelp(DatabaseManagerCommon.scriptHelp);
        } else if (s.equals("SHUTDOWN")) {
            showHelp(DatabaseManagerCommon.shutdownHelp);
        } else if (s.equals("SET")) {
            showHelp(DatabaseManagerCommon.setHelp);
        } else if (s.equals("Test Script")) {
            showHelp(DatabaseManagerCommon.testHelp);
        } else if (s.equals(SHOWSCHEMAS_BOX_TEXT)) {
            showSchemas = boxShowSchemas.isSelected();
            refreshTree();
        } else {
            throw new RuntimeException("Unexpected action triggered: " + s);
        }
    }
