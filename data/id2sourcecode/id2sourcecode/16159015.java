            public void actionPerformed(ActionEvent e) {
                Iterator<WrappedChannel> iter_inner = wrpV.iterator();
                while (iter_inner.hasNext()) {
                    WrappedChannel wch = iter_inner.next();
                    TableRecord liveTR = liveDataTableModel.getRecord(wch.getChannelName());
                    if (wch.isGood()) {
                        liveTR.setValue(wch.getValue());
                        liveTR.setStatus(true);
                    } else {
                        liveTR.setValue(0.);
                        liveTR.setStatus(false);
                        messageTextLocal.setText("Cannot read PV: " + wch.getChannelName());
                    }
                }
                liveDataTableModel.fireTableDataChanged();
            }
