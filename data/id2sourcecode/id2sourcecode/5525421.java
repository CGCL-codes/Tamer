    public HardwareData makePersistent(HardwareData data) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(Environment.getWorkingFolderAbsolute() + Environment.SEPARATOR + FilePersistenceUtil.dataDirectoryRelative + Environment.SEPARATOR + filename, true));
            bw.write("[" + data.getTimestamp().toString() + "] " + data.getHardwareAddrRef() + ", CH-" + data.getChannel() + ", " + data.getDataString());
            bw.newLine();
            bw.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException ioe2) {
                }
            }
        }
        return data;
    }
