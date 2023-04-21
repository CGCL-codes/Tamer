    public void writeChunk(ThreadedChunkLoaderPending threadedchunkloaderpending) throws IOException {
        DataOutputStream dataoutputstream = RegionFileCache.getChunkOutputStream(chunkSaveLocation, threadedchunkloaderpending.field_40739_a.chunkXPos, threadedchunkloaderpending.field_40739_a.chunkZPos);
        CompressedStreamTools.writeTo(threadedchunkloaderpending.field_40738_b, dataoutputstream);
        dataoutputstream.close();
    }
