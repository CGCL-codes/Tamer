            public void actionPerformed(ActionEvent e) {
                for (int i = 0, n = measuredValuesV.size(); i < n; i++) {
                    if (measuredValuesShowStateV.get(i).booleanValue()) {
                        MeasuredValue mv_tmp = measuredValuesV.get(i);
                        if (scanPV_ShowState || scanVariable.getChannel() == null) {
                            mv_tmp.removeAllDataContainersNonRB();
                        }
                        if (scanPV_RB_ShowState) {
                            mv_tmp.removeAllDataContainersRB();
                        }
                    }
                }
                updateDataSetOnGraphPanel();
                messageTextLocal.setText(null);
            }
