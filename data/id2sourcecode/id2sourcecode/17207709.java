    public void sendFiles(OutputStream os) throws Exception {
        BufferedOutputStream bos = new BufferedOutputStream(os);
        ObjectOutputStream objOut = new ObjectOutputStream(os);
        objOut.writeInt(removeFiles.size());
        objOut.writeInt(files.size());
        objOut.flush();
        for (File remFile : removeFiles) {
            objOut.writeObject(remFile.getPath());
        }
        objOut.flush();
        for (File file : files) {
            FileTransferHeader header;
            if (file.isDirectory()) {
                header = new FileTransferHeader(file.getPath(), -1);
            } else {
                header = new FileTransferHeader(file.getPath(), file.length());
            }
            objOut.writeObject(header);
        }
        objOut.flush();
        for (File file : files) {
            if (file.length() != 0L) {
                FileInputStream fis = new FileInputStream(file);
                int read = 0;
                while ((read = fis.read(buffer)) > 0) {
                    bos.write(buffer, 0, read);
                }
                bos.flush();
                fis.close();
            }
        }
        objOut.close();
        bos.close();
    }
