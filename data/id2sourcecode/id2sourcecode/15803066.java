    private BufferedReader openFileToLine(File file, FileLinePointer fileLinePointer) throws IOException {
        randFile = new RandomAccessFile(file, "r");
        randFile.seek(fileLinePointer.getFilePointer());
        channel = randFile.getChannel();
        if (fileLinePointer.getFilePointer() > 0) {
            channel.position(fileLinePointer.getFilePointer());
        }
        return new BufferedReader(new InputStreamReader(Channels.newInputStream(channel)));
    }
