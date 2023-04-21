        private void readTableData(Vector tv, GetData g) {
            tempVec = tv;
            tableDataMap = new ReadExcel().readTable(tempVec.get(0).toString(), file);
            temp = (Vector) tableDataMap.get("Old Table Name");
            oldtablename = temp.get(0).toString();
            oldcolNameVec = (java.util.Vector) tableDataMap.get("Old Column Name");
            oldcoldtVec = (java.util.Vector) tableDataMap.get("Old Column Type");
            temp = (Vector) tableDataMap.get("New Table Name");
            newtablename = temp.get(0).toString();
            newcolNameVec = (java.util.Vector) tableDataMap.get("New Column Name");
            int count = newcolNameVec.size();
            Vector oldTempVec, newTempVec, newTempdtvec, oldTempdtvec;
            oldTempdtvec = new Vector();
            oldTempVec = new Vector();
            newcoldtVec = (java.util.Vector) tableDataMap.get("New Column Type");
            newTempVec = new Vector();
            newTempdtvec = new Vector();
            Object o;
            Vector lastcolVec = new Vector();
            Vector lastcoltypeVec = new Vector();
            for (int i = 0; i < count; i++) {
                if (!(newcolNameVec.get(i).toString().equalsIgnoreCase("null"))) {
                    try {
                        o = oldcolNameVec.get(i);
                        if (o == null || o.equals("") || o.equals("null")) {
                            lastcolVec.addElement(newcolNameVec.get(i));
                            lastcoltypeVec.addElement(newcoldtVec.get(i));
                        } else {
                            oldTempVec.addElement(o);
                            oldTempdtvec.addElement(oldcoldtVec.get(i));
                            newTempVec.addElement(newcolNameVec.get(i));
                            newTempdtvec.addElement(newcoldtVec.get(i));
                        }
                    } catch (ArrayIndexOutOfBoundsException aoe) {
                        if (newcolNameVec.size() > i) {
                            for (int k = i; k < count; k++) {
                                newTempVec.addElement(newcolNameVec.get(k));
                                newTempdtvec.addElement(newcoldtVec.get(k));
                            }
                        }
                    }
                }
            }
            newcolNameVec = new Vector();
            newcolNameVec = newTempVec;
            if (lastcolVec.size() > 0) newcolNameVec.addAll(lastcolVec);
            newcoldtVec = new Vector();
            newcoldtVec = newTempdtvec;
            if (lastcoltypeVec.size() > 0) newcoldtVec.addAll(newcoldtVec);
            oldcolNameVec = new Vector();
            oldcolNameVec = oldTempVec;
            oldcoldtVec = new Vector();
            oldcoldtVec = oldTempdtvec;
            oldcolNameVec = new Vector();
            oldcolNameVec = oldTempVec;
            oldcoldtVec = new Vector();
            oldcoldtVec = oldTempdtvec;
            oldTempVec = null;
            newTempVec = null;
            newTempdtvec = null;
            oldTempdtvec = null;
            int mn = g.getRowNumber(oldtablename, srcseldriver, desselcdriver)[0];
            int mx = g.getRowNumber(oldtablename, srcseldriver, desselcdriver)[1];
            int rowCnt = mx - mn;
            if (rowCnt == 0) {
                gbc.gridx = 0;
                gbc.gridy = y;
                gbc.anchor = GridBagConstraints.WEST;
                migStatus = "yes";
                g.closeOldConn();
                g.closeNewConn();
                _dispPanel.add(new JLabel("The table " + newtablename + " is migrated"), gbc);
                y++;
            }
            while (rowCnt != 0) {
                if (rowCnt > 10000) {
                    rowCnt = rowCnt - 10000;
                    colDataVec = g.getTableData(oldtablename, oldcolNameVec, oldcoldtVec, mn, mn + 10000, srcseldriver, desselcdriver);
                    mn = mn + 10001;
                    Vector tempColDataVec = getConvertedData(oldcolNameVec, newcolNameVec, oldcoldtVec, newcoldtVec, null, null, colDataVec);
                    colDataVec = new Vector();
                    if (tempColDataVec != null) colDataVec = tempColDataVec; else {
                        f = g.rollBack(newtablename);
                        final JLabel lbl = new JLabel("The table " + newtablename + " cannot be migrated");
                        gbc.gridx = 0;
                        gbc.gridy = y;
                        gbc.anchor = GridBagConstraints.WEST;
                        _dispPanel.add(lbl, gbc);
                        gbc.gridx = 1;
                        gbc.gridy = y;
                        btn = new JButton("Re-Migrate");
                        btn.addActionListener(new ActionListener() {

                            public void actionPerformed(java.awt.event.ActionEvent ae) {
                                migStatus = "no";
                                Vector v = new Vector();
                                v.addElement(oldtablename);
                                GetConn obj = new GetConn(v);
                                ThrdCnt++;
                                obj.start();
                                SwingUtilities.invokeLater(new Runnable() {

                                    public void run() {
                                        if (migStatus.equalsIgnoreCase("yes")) {
                                            btn.setVisible(false);
                                            lbl.setText("The table " + oldtablename + " is migrated");
                                        }
                                    }
                                });
                            }
                        });
                        gbc.anchor = GridBagConstraints.WEST;
                        _dispPanel.add(btn, gbc);
                        y++;
                        gbc.gridx = 0;
                        gbc.gridy = y;
                        gbc.anchor = GridBagConstraints.CENTER;
                        g.closeOldConn();
                        g.closeNewConn();
                        _dispPanel.add(new JLabel("The data is incorrect in " + oldcolnm), gbc);
                        y++;
                        break;
                    }
                    g.insertColData(newtablename, newcolNameVec, colDataVec);
                } else {
                    colDataVec = g.getTableData(oldtablename, oldcolNameVec, oldcoldtVec, mn, mx, srcseldriver, desselcdriver);
                    Vector tempColDataVec = getConvertedData(oldcolNameVec, newcolNameVec, oldcoldtVec, newcoldtVec, null, null, colDataVec);
                    if (tempColDataVec != null) colDataVec = tempColDataVec; else {
                        f = g.rollBack(newtablename);
                        gbc.gridx = 0;
                        gbc.gridy = y;
                        final JLabel lbl = new JLabel("The table " + newtablename + " cannot be migrated");
                        gbc.anchor = GridBagConstraints.WEST;
                        _dispPanel.add(lbl, gbc);
                        gbc.gridx = 1;
                        gbc.gridy = y;
                        btn = new JButton("Re-Migrate");
                        btn.addActionListener(new ActionListener() {

                            public void actionPerformed(java.awt.event.ActionEvent ae) {
                                migStatus = "no";
                                Vector v = new Vector();
                                v.addElement(oldtablename);
                                GetConn obj = new GetConn(v);
                                ThrdCnt++;
                                obj.start();
                                SwingUtilities.invokeLater(new Runnable() {

                                    public void run() {
                                        if (migStatus.equalsIgnoreCase("yes")) {
                                            btn.setVisible(false);
                                            lbl.setText("The table " + oldtablename + " is migrated");
                                        }
                                    }
                                });
                            }
                        });
                        _dispPanel.add(btn, gbc);
                        y++;
                        g.closeOldConn();
                        g.closeNewConn();
                        gbc.gridx = 0;
                        gbc.gridy = y;
                        _dispPanel.add(new JLabel("The data is incorrect in " + oldcolnm), gbc);
                        y++;
                        return;
                    }
                    colDataVec = new Vector();
                    colDataVec = tempColDataVec;
                    g.insertColData(newtablename, newcolNameVec, colDataVec);
                    rowCnt = 0;
                    g.closeOldConn();
                    g.closeNewConn();
                    gbc.gridx = 0;
                    gbc.gridy = y;
                    gbc.anchor = GridBagConstraints.WEST;
                    migStatus = "yes";
                    _dispPanel.add(new JLabel("The table " + newtablename + " is migrated"), gbc);
                    y++;
                }
            }
        }
