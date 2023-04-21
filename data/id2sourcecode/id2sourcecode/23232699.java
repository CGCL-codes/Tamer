    public boolean open(boolean bAppend) {
        try {
            FileIOAccounting.startFileIO(FileIOAccountingType.Open);
            FileOutputStream fileOutput = new FileOutputStream(getName(), bAppend);
            m_out = new BufferedOutputStream(new DataOutputStream(fileOutput));
            FileChannel outChannel = fileOutput.getChannel();
            try {
                m_outLock = outChannel.lock();
            } catch (IOException e) {
                FileIOAccounting.endFileIO();
                e.printStackTrace();
                return false;
            }
            FileIOAccounting.endFileIO();
            return true;
        } catch (FileNotFoundException e) {
            FileIOAccounting.endFileIO();
            e.printStackTrace();
        }
        return false;
    }
