    private void open() throws FileNotFoundException, IOException {
        fileOutput = new FileOutputStream(file, true);
        bufferedOutput = new BufferedOutputStream(fileOutput, bufferSize);
        dataOutput = new DataOutputStream(bufferedOutput);
        fileOutput.getFD().sync();
        offset = fileOutput.getChannel().size();
    }
