        public void updateData(String name, double time, double data) {
            cleared = false;
            String chanUnit = null;
            Channel channel = rbnbController.getChannel(name);
            if (channel != null) chanUnit = channel.getMetadata("units");
            for (int i = 0; i < rows.size(); i++) {
                DataRow dataRow = (DataRow) rows.get(i);
                if (dataRow.name.equals(name)) {
                    dataRow.setData(time, data, chanUnit);
                    fireTableDataChanged();
                    break;
                }
            }
        }
