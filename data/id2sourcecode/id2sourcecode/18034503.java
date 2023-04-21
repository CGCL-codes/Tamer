    public void putFile(CompoundName file, FileInputStream fileInput) throws IOException {
        System.out.println("PUTTING file " + file);
        createDirs(file, 0);
        FTPClient client = initFTPSession();
        for (int i = 0; i < file.size() - 1; i++) client.cd(file.get(i));
        OutputStream outStream = client.put(file.get(file.size() - 1));
        for (int byteIn = fileInput.read(); byteIn != -1; byteIn = fileInput.read()) outStream.write(byteIn);
        fileInput.close();
        outStream.close();
        client.closeServer();
    }
