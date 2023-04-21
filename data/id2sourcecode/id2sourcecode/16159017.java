            public void actionPerformed(ActionEvent e) {
                int[] inds = { 6, 7, 8, 0, 1, 2 };
                for (int i = 0; i < inds.length; i++) {
                    int ind = inds[i];
                    WrappedChannel wch = wrpV.get(ind);
                    TableRecord memTR = memorizedDataTableModel.getRecord(wch.getChannelName());
                    if (memTR.getStatus()) {
                        wch.setValue(memTR.getValue());
                    }
                }
            }
