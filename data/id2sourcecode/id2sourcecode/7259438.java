            public void actionPerformed(ActionEvent event) {
                if (pvLogSelector == null) pvLogSelector = plsc.choosePVLogId(); else pvLogSelector.setVisible(true);
                mxProxy.setChannelSource(ModelProxy.PARAMSRC_DESIGN);
                MPXMain.PARAM_SRC = mxProxy.getChannelSource();
                syncModelAction.setEnabled(true);
                usePVLog = true;
            }
