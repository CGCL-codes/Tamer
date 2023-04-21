    public ArrayList<String> getTimeList() {
        timeList.clear();
        if (splitedData == null) {
            return null;
        } else if (!readCheckBox.isSelected() && !writeCheckBox.isSelected()) {
            return null;
        } else {
            int readSize = 0;
            int writeSize = 0;
            if (splitedData[0] != null) {
                readSize = splitedData[0].getData_timed().length;
            }
            if (splitedData[1] != null) {
                writeSize = splitedData[1].getData_timed().length;
            }
            if (readCheckBox.isSelected()) {
                if (writeCheckBox.isSelected()) {
                    int size = Math.max(readSize, writeSize);
                    for (int i = 0; i < size; i++) {
                        DataArray readDataArray = extractDataArray(i, splitedData[0]);
                        boolean filled = false;
                        if (readDataArray != null) {
                            timeList.add(readDataArray.getId());
                        }
                        readDataArray = null;
                        DataArray writeDataArray = extractDataArray(i, splitedData[1]);
                        if (writeDataArray != null) {
                            if (!filled) {
                                timeList.add(writeDataArray.getId());
                            }
                        }
                    }
                } else {
                    for (int i = 0; i < readSize; i++) {
                        DataArray readDataArray = extractDataArray(i, splitedData[0]);
                        if (readDataArray != null) {
                            if (cleaning) {
                                return null;
                            }
                            timeList.add(readDataArray.getId());
                        }
                        readDataArray = null;
                    }
                }
            } else {
                for (int i = 0; i < writeSize; i++) {
                    DataArray writeDataArray = extractDataArray(i, splitedData[1]);
                    if (writeDataArray != null) {
                        timeList.add(writeDataArray.getId());
                    }
                }
            }
        }
        return timeList;
    }
