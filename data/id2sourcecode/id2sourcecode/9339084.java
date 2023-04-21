    @Override
    public Phd get(String id) throws DataStoreException {
        checkIfNotYetInitialized();
        FileChannel fastaFileChannel = null;
        DefaultPhdFileDataStore dataStore = null;
        InputStream in = null;
        FileInputStream fileInputStream = null;
        try {
            if (!recordLocations.contains(id)) {
                throw new DataStoreException(id + " does not exist");
            }
            Range range = recordLocations.getRangeFor(id);
            fileInputStream = new FileInputStream(phdBall);
            MappedByteBuffer buf = fileInputStream.getChannel().map(FileChannel.MapMode.READ_ONLY, range.getStart(), range.size());
            in = new ByteBufferInputStream(buf);
            dataStore = new DefaultPhdFileDataStore();
            PhdParser.parsePhd(in, dataStore);
            return dataStore.get(id);
        } catch (IOException e) {
            throw new DataStoreException("error getting " + id, e);
        } finally {
            IOUtil.closeAndIgnoreErrors(fastaFileChannel);
            IOUtil.closeAndIgnoreErrors(dataStore);
            IOUtil.closeAndIgnoreErrors(in);
            IOUtil.closeAndIgnoreErrors(fileInputStream);
        }
    }
