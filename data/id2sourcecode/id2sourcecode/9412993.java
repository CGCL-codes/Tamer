    public void rolloverIfNecessary() throws IOException {
        long creationTime = readCreationTime();
        FileChannel activeFileChannel = fileOut.getChannel();
        long fileLength = activeFileChannel.size();
        Date creationDate = new Date(creationTime);
        boolean rolloverNow = strategy.rolloverNow(creationDate, fileLength);
        if (rolloverNow) {
            synchronized (WRITER_CHANGE_GATE) {
                synchronized (PRINTERS_SEMAPHORE) {
                    if (printerCount != 0) {
                        try {
                            PRINTERS_SEMAPHORE.wait();
                        } catch (InterruptedException e) {
                            throw new IllegalStateException("Interrupted while attempting to configure writer");
                        }
                    }
                    writer = tempWriter;
                }
            }
            int fileIdNumber = uniqueFileId++;
            StringBuffer fileIdString = new StringBuffer();
            fileIdString.append(Integer.toHexString(fileIdNumber).toUpperCase());
            char[] zeroes = new char[8 - fileIdString.length()];
            Arrays.fill(zeroes, '0');
            fileIdString.insert(0, zeroes);
            String rolloverFileName = rolloverLogFileFormat.format(new Object[] { new Date(), fileIdString.toString() });
            if (!rolloverDirectory.exists()) rolloverDirectory.mkdirs();
            File rolloverFile = new File(rolloverDirectory, rolloverFileName);
            FileOutputStream rolloverOut = new FileOutputStream(rolloverFile, false);
            FileChannel rolloverChannel = rolloverOut.getChannel();
            activeFileChannel.transferTo(0, activeFileChannel.size(), rolloverChannel);
            rolloverOut.close();
            activeFileChannel.truncate(0);
            fileOut.seek(0);
            Writer newFileWriter = Channels.newWriter(activeFileChannel, "UTF-8");
            File creationTimeFile = getCreationTimeFile(currentActiveLogFileDirectory, currentActiveLogFile);
            creationTimeFile.delete();
            storeFileCreationTimeIfNecessary(currentActiveLogFileDirectory, currentActiveLogFile);
            synchronized (WRITER_CHANGE_GATE) {
                synchronized (PRINTERS_SEMAPHORE) {
                    if (printerCount != 0) {
                        try {
                            PRINTERS_SEMAPHORE.wait();
                        } catch (InterruptedException e) {
                            throw new IllegalStateException("Interrupted while attempting to configure writer");
                        }
                    }
                    writer = newFileWriter;
                    StringBuffer tempBuffer = tempWriter.getBuffer();
                    newFileWriter.write(tempBuffer.toString());
                    tempBuffer.delete(0, tempBuffer.length());
                }
            }
        }
    }
