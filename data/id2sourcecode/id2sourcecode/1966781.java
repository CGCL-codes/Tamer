    private void receiveDeltas() {
        if (recvFile == null) {
            recvFile = files.get(recvIndex);
            basisFile = new File(path, recvFile.dirname + File.separator + recvFile.baseName);
            if (basisFile.exists()) rebulider.setBasisFile(basisFile); else rebuilder.setBasisFile(null);
            tempfile = File.createTempFile("." + recvFile.basename, "", new File(path));
            targetOut = new RandomAccessFile(tempfile, "rw");
            targetOut.setLength(recvFile.length);
            if (recvFile.length > MAP_LIMIT) {
                targetMap = targetOut.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, MAP_SIZE);
                mapOffset = 0L;
            }
        } else {
            if (residue > 0) {
                int len = Math.min(residue, Math.min(inBuffer.remaining(), recvBuffer.length));
            } else {
                int tag = inBuffer.getInt();
                if (tag < 0) {
                    long offset = (-token + 1) * (long) config.blockLength;
                    int len = (int) (basisFile.length() - offset) < config.blockLength ? (int) (basisFile.length() - offset) : config.blockLength;
                    rebuilder.update(new Offsets(offset, recvOffset, len));
                    recvOffset += len;
                } else if (tag > 0) {
                    residue = tag;
                    int len = Math.min(residue, Math.min(inBuffer().remaining, recvBuffer.length));
                    inBuffer.get(recvBuffer, 0, len);
                    if (len <= residue) residue -= len; else residue = 0;
                    rebuilder.update(new DataBlock(recvOffset, recvBuffer, 0, len));
                    recvOffset += len;
                } else {
                    targetOut.close();
                    tempfile.renameTo(basisFile);
                }
            }
        }
    }
