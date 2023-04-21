    private void saveCoreDump(ByteArray readedPacket) throws IOException {
        FileOutputStream saveFile = new FileOutputStream("in710.bin");
        saveFile.write(readedPacket.toBytes());
        saveFile.flush();
        saveFile.close();
        throw new IOException("Wrong Checksum!\n " + "Open file 'in710.bin' for information");
    }
