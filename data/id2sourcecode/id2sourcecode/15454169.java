    public static DefaultStackDataArrayDAO createSpectrumStackDAO(JLDataView[] readView, JLDataView[] writeView) {
        int readViewSize = 0;
        int writeViewSize = 0;
        if (readView != null) {
            readViewSize = readView.length;
        } else if (writeView != null) {
            writeViewSize = writeView.length;
        }
        StackDataArray data = new StackDataArray();
        for (int i = 0; i < Math.max(readViewSize, writeViewSize); i++) {
            ArrayList<DataArray> dataArrayList = new ArrayList<DataArray>();
            if (readView != null) {
                if (readView[i] != null) {
                    JLDataView dvRead = readView[i];
                    DataArray dataArrayRead = new DataArray();
                    dataArrayRead.setName(dvRead.getName());
                    DataList dlRead = dvRead.getData();
                    DataList currentRead = dlRead;
                    while (currentRead != null) {
                        dataArrayRead.add(currentRead.x, currentRead.y);
                        currentRead = currentRead.next;
                    }
                    dataArrayList.add(dataArrayRead);
                }
            }
            if (writeView != null) {
                if (writeView[i] != null) {
                    JLDataView dvWrite = writeView[i];
                    DataArray dataArrayWrite = new DataArray();
                    dataArrayWrite.setName(dvWrite.getName());
                    DataList dlWrite = dvWrite.getData();
                    DataList currentWrite = dlWrite;
                    while (currentWrite != null) {
                        dataArrayWrite.add(currentWrite.x, currentWrite.y);
                        currentWrite = currentWrite.next;
                    }
                    dataArrayList.add(dataArrayWrite);
                }
            }
            data.add(dataArrayList);
        }
        DefaultStackDataArrayDAO dao = new DefaultStackDataArrayDAO();
        dao.setData(data);
        return dao;
    }
