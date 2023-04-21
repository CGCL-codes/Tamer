    private void chopFile() throws IOException {
        long fileLength = logFile.length();
        RandomAccessFile raf = null;
        BufferedOutputStream bos = null;
        if (fileLength < 1) {
            return;
        }
        try {
            raf = new RandomAccessFile(logFile, "r");
            raf.seek(fileLength / reductionRatio);
            byte readBuffer[] = new byte[(int) (fileLength - (fileLength / reductionRatio))];
            raf.read(readBuffer);
            raf.close();
            bos = new BufferedOutputStream(new FileOutputStream(logFile));
            bos.write(readBuffer);
        } catch (OutOfMemoryError ex) {
            try {
                chopFileDisk();
            } catch (IOException EX) {
                throw new IOException("Unable to reduce the size of the " + logFile.getName() + " file. Disk full and not enough ram.");
            }
        } finally {
            if (raf != null) {
                try {
                    raf.close();
                } catch (IOException ex) {
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException ex) {
                }
            }
        }
    }
