        public Object getValueAt(int rowIndex, int columnIndex) {
            MeasurementGroup mg = null;
            int measurementGroupsLen = measurementGroups.size();
            if (rowIndex < measurementGroupsLen) {
                mg = measurementGroups.get(rowIndex);
            }
            if (mg == null) return null;
            if (columnIndex == 0) {
                JLabel l = new JLabel(mg.getState());
                l.setOpaque(true);
                return l;
            } else if (columnIndex == 2) return mg.getDeviceName(); else if (columnIndex == 3) return mg.getChannels(); else return mg;
        }
