    public void readDissimilarityFile(String filename, ProgressFrame progress) throws IOException {
        try {
            ProcessTimer disReadTimer = new ProcessTimer("AbstractCluster.readDissimilarityFile()");
            FileChannel dysChannel = new FileInputStream(filename).getChannel();
            DataInputStream dysHeaders = new DataInputStream(Channels.newInputStream(dysChannel));
            readDisHeaders(dysHeaders);
            labels = new ArrayList(numGenes);
            for (int count = 0; count < numGenes; count++) labels.add(dysHeaders.readUTF());
            MappedByteBuffer dysBuffer = dysChannel.map(FileChannel.MapMode.READ_ONLY, dysChannel.position(), dysChannel.size() - dysChannel.position());
            dysHeaders.close();
            dys = new float[numGenes - 1][];
            numPairs = (numGenes * (numGenes - 1)) / 2;
            if (progress != null) progress.setMaximum(numPairs);
            for (int row = 1; row < numGenes && !cancel; row++) {
                dys[row - 1] = new float[row];
                for (int column = 0; column < row; column++) {
                    dys[row - 1][column] = dysBuffer.getFloat();
                    if (progress != null) progress.addValue(1);
                }
            }
            disReadTimer.finish();
        } catch (Exception e) {
            throw new IOException();
        }
    }
