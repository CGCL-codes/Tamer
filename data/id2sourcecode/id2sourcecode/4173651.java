    private void dealPartnerThings(String action) {
        if (action.equals("ADD")) {
            Cell cell = findCell(getRow(), "P", "NS");
            if ((cell != null) && (!cell.getColumnValue().equals("ME"))) {
                try {
                    String type = "Confirm Add Account";
                    Iterator rowIter = getRow().getRowSet().getRows().iterator();
                    Cell pCell = findCell(getRow(), "P", "DEFPA");
                    while (rowIter.hasNext()) {
                        Row childRow = (Row) rowIter.next();
                        Cell cCell = findCell(childRow, "PA", "PA");
                        if ((childRow != getRow()) && (cCell.getColumnValue().equals(pCell.getColumnValue()))) {
                            CIXFileData data = new CIXFileData(type, getPrivateKey(), childRow);
                            CIXFile cixFile = new CIXFile(getPublicKey(), data);
                            Random random = new Random();
                            String fileName = "confirm_add_account_" + random.nextInt(25000);
                            cixFile.exportFile(fileName);
                            String str = TxtManager.getTxt("PARTNER.IMPORT.ADDPARTNER");
                            str = str.replaceAll("%", fileName);
                            DialogManager.showMessageDialog(getWindow(), str);
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if ((cell != null) && (cell.getColumnValue().equals("ME"))) {
                try {
                    KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
                    keyGen.initialize(1024);
                    KeyPair key = keyGen.generateKeyPair();
                    Iterator rowIter = getRow().getRowSet().getRows().iterator();
                    while (rowIter.hasNext()) {
                        Row childRow = (Row) rowIter.next();
                        if (childRow != getRow()) {
                            Cell publicKeyCell = findCell(childRow, "PA", "PUBKEY");
                            Cell privateKeyCell = findCell(childRow, "PA", "PRIKEY");
                            publicKeyCell.setColumnValue(key.getPublic());
                            privateKeyCell.setColumnValue(key.getPrivate());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (action.equals("Confirm Add Account Request")) {
            Cell cell = findCell(getRow(), "P", "NS");
            if ((cell != null) && (!cell.getColumnValue().equals("ME"))) {
                try {
                    Iterator rowIter = getRow().getRowSet().getRows().iterator();
                    while (rowIter.hasNext()) {
                        Row childRow = (Row) rowIter.next();
                        if (childRow != getRow()) {
                            Cell publicKeyCell = findCell(childRow, "PA", "PUBKEY");
                            publicKeyCell.setColumnValue(getCixFile().getPublicKey());
                        }
                    }
                    String type = "Re Confirm Add Account";
                    rowIter = getRow().getRowSet().getRows().iterator();
                    Cell pCell = findCell(getRow(), "P", "DEFPA");
                    while (rowIter.hasNext()) {
                        Row childRow = (Row) rowIter.next();
                        Cell cCell = findCell(childRow, "PA", "PA");
                        if ((childRow != getRow()) && (cCell.getColumnValue().equals(pCell.getColumnValue()))) {
                            CIXFileData data = new CIXFileData(type, getPrivateKey(), childRow);
                            CIXFile cixFile = new CIXFile(getPublicKey(), data);
                            Random random = new Random();
                            String fileName = "re_confirm_add_account_" + random.nextInt(25000);
                            cixFile.exportFile(fileName);
                            String str = TxtManager.getTxt("PARTNER.IMPORT.CONFIRMADDPARTNER");
                            str = str.replaceAll("%", fileName);
                            DialogManager.showMessageDialog(getWindow(), str);
                            this.cixFile.getData().setType("Used");
                            this.cixFile.exportFile("used_" + getFile().getName().substring(0, getFile().getName().length() - 2));
                            file.delete();
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (action.equals("Re Confirm Add Account")) {
            MainView container = (MainView) window;
            JTree tree = container.getTree();
            Row tRow = cixFile.getData().getRow(cixFile.getPublicKey());
            Cell cell1 = findCell(tRow, "P", "NS");
            Cell cell2 = findCell(tRow, "P", "P");
            Cell cell3 = findCell(tRow, "P", "URNS");
            Cell cell4 = findCell(tRow, "P", "URP");
            for (int i = 1; i <= tree.getRowCount(); i++) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getPathForRow(i).getLastPathComponent();
                if (node != null) {
                    ObjectInfo oj = (ObjectInfo) node.getUserObject();
                    if ((oj.flag.equals("object")) && (oj.clsId == 250)) {
                        tree.setSelectionPath(tree.getPathForRow(i));
                        Iterator rowIter = container.getCurrentObject().getRows().iterator();
                        boolean findRow = false;
                        while (rowIter.hasNext()) {
                            Row cRow = (Row) rowIter.next();
                            Cell ccell1 = findCell(cRow, "P", "NS");
                            Cell ccell2 = findCell(cRow, "P", "P");
                            if ((ccell1.getColumnValue().equals(cell3.getColumnValue())) && (ccell2.getColumnValue().equals(cell4.getColumnValue()))) {
                                findRow = true;
                                Iterator childRowIter = cRow.getRowSet().getRows().iterator();
                                while (childRowIter.hasNext()) {
                                    Row childRow = (Row) childRowIter.next();
                                    childRow.setModify(true);
                                    Cell ccell3 = findCell(childRow, "P", "URNS");
                                    Cell ccell4 = findCell(childRow, "P", "URP");
                                    Cell publicKeyCell = findCell(childRow, "PA", "PUBKEY");
                                    ccell3.setColumnValue(cell1.getColumnValue());
                                    ccell4.setColumnValue(cell2.getColumnValue());
                                    publicKeyCell.setColumnValue(cixFile.getPublicKey());
                                }
                                container.getCurrentObject().classUpdate_WholeObject(cRow, false);
                                break;
                            }
                        }
                        if (findRow) {
                            tree.setSelectionPath(tree.getPathForRow(i));
                            String str = TxtManager.getTxt("PARTNER.IMPORT.RECONFIRMADDPARTNER");
                            str = str.replaceAll("%", cell3.getColumnValue().toString());
                            str = str.replaceAll("#", cell4.getColumnValue().toString());
                            DialogManager.showMessageDialog(container, str);
                        } else {
                            String str = TxtManager.getTxt("PARTNER.IMPORT.RECONFIRMADDPARTNER.FAIL");
                            DialogManager.showMessageDialog(container, str);
                        }
                        this.cixFile.getData().setType("Used");
                        this.cixFile.exportFile("used_" + getFile().getName().substring(0, getFile().getName().length() - 2));
                        file.delete();
                        break;
                    }
                }
            }
        }
    }
