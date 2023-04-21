    @Override
    public Phd get(String id) throws DataStoreException {
        FileChannel fastaFileChannel = null;
        PhdDataStore dataStore = null;
        InputStream in = null;
        FileInputStream fileInputStream = null;
        try {
            if (!recordLocations.contains(id)) {
                throw new DataStoreException(id + " does not exist");
            }
            Range range = recordLocations.getRangeFor(id);
            fileInputStream = new FileInputStream(phdBall);
            MappedByteBuffer buf = fileInputStream.getChannel().map(FileChannel.MapMode.READ_ONLY, range.getBegin(), range.getLength());
            in = new ByteBufferInputStream(buf);
            PhdDataStoreBuilder builder = DefaultPhdFileDataStore.createBuilder();
            PhdParser.parsePhd(in, builder);
            dataStore = builder.build();
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
