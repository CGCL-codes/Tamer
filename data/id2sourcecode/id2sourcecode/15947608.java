            public Object getValueAt(int row, int col) {
                ArrayViewerPV arrPV = (ArrayViewerPV) arrayPVs.get(row);
                return arrPV.getChannelName();
            }
