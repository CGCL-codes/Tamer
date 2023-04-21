    public void writeToken(FileToken fileTokenDTO, TransmissionCallback transmissionCallback) {
        String fullFileName = "/cinbox" + fileTokenDTO.getPath() + JefbUtils.FILE_SEPARATOR + fileTokenDTO.getFilename();
        try {
            FileChannel targetChannel = new RandomAccessFile(fullFileName, "rw").getChannel();
            MappedByteBuffer targetBuffer = targetChannel.map(FileChannel.MapMode.READ_WRITE, fileTokenDTO.getOffset(), fileTokenDTO.getLength());
            targetBuffer.put(fileTokenDTO.getData());
            targetChannel.close();
        } catch (FileNotFoundException fnfe) {
            transmissionCallback.getMessages().add("Unable to find file named:" + fullFileName);
        } catch (IOException e) {
            transmissionCallback.getMessages().add("Unable to access target file:" + fullFileName);
        } catch (Exception e) {
            transmissionCallback.getMessages().add("Unexpected exception by writing token for file:" + fullFileName);
        }
    }
