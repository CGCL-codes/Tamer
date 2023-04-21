            public Object getValueAt(int row, int col) {
                return spvs.getScalarPV(row).getMonitoredPV().getChannelName();
            }
