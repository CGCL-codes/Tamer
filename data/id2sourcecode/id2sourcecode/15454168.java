    public static DefaultDataArrayDAO createSpectrumDAO(JLDataView[] readView, JLDataView[] writeView) {
        List<DataArray> dataArrayList = new ArrayList<DataArray>();
        if (readView != null) {
            for (int i = 0; i < readView.length; i++) {
                JLDataView dv = readView[i];
                DataArray dataArray = new DataArray();
                dataArray.setName(dv.getName());
                DataList dl = dv.getData();
                DataList current = dl;
                while (current != null) {
                    dataArray.add(current.x, current.y);
                    current = current.next;
                }
                dataArrayList.add(dataArray);
            }
        }
        if (writeView != null) {
            for (int i = 0; i < writeView.length; i++) {
                JLDataView dv = writeView[i];
                DataArray dataArray = new DataArray();
                dataArray.setName(dv.getName());
                DataList dl = dv.getData();
                DataList current = dl;
                while (current != null) {
                    dataArray.add(current.x, current.y);
                    current = current.next;
                }
                dataArrayList.add(dataArray);
            }
        }
        DefaultDataArrayDAO dao = new DefaultDataArrayDAO();
        dao.setData(dataArrayList);
        return dao;
    }
