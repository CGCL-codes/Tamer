    public boolean exclusiveLockFile() {
        try {
            FileOutputStream fileOutput = new FileOutputStream(m_csMakerPath, false);
            m_out = new BufferedOutputStream(new DataOutputStream(fileOutput));
            FileChannel outChannel = fileOutput.getChannel();
            try {
                m_outLock = outChannel.lock();
            } catch (IOException e) {
                return false;
            }
            return true;
        } catch (FileNotFoundException e) {
            return false;
        }
    }
