            public void actionPerformed(ActionEvent e) {
                Iterator<WrappedChannel> iter_inner = wrpV.iterator();
                while (iter_inner.hasNext()) {
                    WrappedChannel wch = iter_inner.next();
                    TableRecord memTR = memorizedDataTableModel.getRecord(wch.getChannelName());
                    if (wch.isGood()) {
                        memTR.setValue(wch.getValue());
                        memTR.setStatus(true);
                    } else {
                        memTR.setValue(0.);
                        memTR.setStatus(false);
                        messageTextLocal.setText("Cannot read PV: " + wch.getChannelName());
                    }
                }
                memorizedDataTableModel.fireTableDataChanged();
            }
