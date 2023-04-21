    public void chunkInBytes(InputStream is, InputFileInfo info, FileProcessorEnvironment env) throws IOException {
        holder.inputFileStarted();
        int chunkCount = 0;
        int chunkSize = getSize();
        int bufferSize = (chunkSize > 1024) ? 1024 : chunkSize;
        int bytesInChunk = 0;
        String initialName = info.getProposedName();
        int readByteCount;
        byte[] buffer = new byte[bufferSize];
        OutputStream os = getOutputStream(info, env, chunkCount, initialName);
        holder.outputFileStarted();
        while (((readByteCount = is.read(buffer)) >= 0) && env.shouldContinue()) {
            if (bytesInChunk + readByteCount > chunkSize) {
                int i = chunkSize - bytesInChunk;
                os.write(buffer, 0, i);
                os.close();
                chunkCount++;
                os = getOutputStream(info, env, chunkCount, initialName);
                holder.outputFileStarted();
                bytesInChunk = readByteCount - i;
                os.write(buffer, i, bytesInChunk);
            } else {
                os.write(buffer, 0, readByteCount);
                bytesInChunk += readByteCount;
            }
        }
        if (os != null) os.close();
    }
