    public ViewSpectrumTableModel(String[] readNames, String[] writeNames) {
        super();
        this.readNames = readNames;
        this.writeNames = writeNames;
        int readLength = Math.max(0, computeReadLength());
        int writeLength = Math.max(0, computeWriteLength());
        selectedRead = new boolean[readLength];
        selectedWrite = new boolean[writeLength];
        for (int i = 0; i < selectedRead.length; i++) {
            selectedRead[i] = true;
        }
    }
