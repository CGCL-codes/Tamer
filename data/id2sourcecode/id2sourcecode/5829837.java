            public void tableChanged(TableModelEvent e) {
                int i, k;
                BatchObject bObj;
                if (e.getSource() == batchTM) {
                    if (e.getType() == TableModelEvent.DELETE) return;
                    k = e.getFirstRow();
                    if (k >= 0 && !batchVector.isEmpty()) {
                        bObj = (BatchObject) batchVector.get(k);
                        if ((bObj.command == BatchObject.CMD_MODULE) && (bObj.modObj.modClass == null)) {
                            final DocumentHandler dh = AbstractApplication.getApplication().getDocumentHandler();
                            final DocumentFrame[] modules = new DocumentFrame[dh.getDocumentCount()];
                            for (int m = 0; m < modules.length; m++) {
                                modules[m] = ((Session) dh.getDocument(m)).getFrame();
                            }
                            String[] winNames;
                            ListDlg dlg;
                            PropertyArray pa;
                            String str;
                            int winNum = modules.length;
                            List v;
                            for (i = 0; i < winNum; i++) {
                                str = modules[i].getClass().getName();
                                str = str.substring(str.lastIndexOf('.') + 1);
                                if ((modules[i] == enc_this) || Util.isValueInArray(str, EXCLUDE_DLG)) {
                                    winNum--;
                                    for (int j = i; j < winNum; j++) {
                                        modules[j] = modules[j + 1];
                                    }
                                    i--;
                                    continue;
                                }
                            }
                            winNames = new String[winNum];
                            for (i = 0; i < winNum; i++) {
                                winNames[i] = modules[i].getTitle();
                            }
                            if (winNum > 1) {
                                dlg = new ListDlg(getWindow(), "Choose Module", winNames);
                                i = dlg.getList();
                            } else {
                                i = winNum - 1;
                            }
                            if (i < 0) return;
                            bObj.modObj.name = winNames[i];
                            ((DocumentFrame) modules[i]).fillPropertyArray();
                            pa = ((DocumentFrame) modules[i]).getPropertyArray();
                            bObj.modObj.prParam = pa.toProperties(true);
                            bObj.modObj.modClass = modules[i].getClass().getName();
                            v = new ArrayList();
                            for (int j = 0; j < pa.text.length; j++) {
                                if (pa.textName[j].indexOf("File") >= 0) {
                                    v.add(new Integer(j));
                                }
                            }
                            bObj.modObj.modParam = new String[v.size()][2];
                            for (int j = 0; j < v.size(); j++) {
                                bObj.modObj.modParam[j][0] = pa.textName[j];
                                bObj.modObj.modParam[j][1] = pa.text[j];
                            }
                            batchTM.fireTableRowsUpdated(k, k);
                            updateParamTable();
                        }
                    }
                } else if (e.getSource() == paramTM) {
                }
            }
