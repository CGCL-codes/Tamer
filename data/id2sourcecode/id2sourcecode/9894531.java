    public void createModifiedCopy(final InputStream source, final OutputStream dest, final List<ChunkModifier> modifiers) throws IOException {
        final List<ChunkModifier> modders = new ArrayList<ChunkModifier>();
        if (modifiers != null) {
            modders.addAll(modifiers);
        }
        final GUID readGUID = Utils.readGUID(source);
        if (GUID.GUID_HEADER.equals(readGUID)) {
            long totalDiff = 0;
            long chunkDiff = 0;
            final long headerSize = Utils.readUINT64(source);
            final long chunkCount = Utils.readUINT32(source);
            final byte[] reserved = new byte[2];
            reserved[0] = (byte) (source.read() & 0xFF);
            reserved[1] = (byte) (source.read() & 0xFF);
            final ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] fileHeader = null;
            for (long i = 0; i < chunkCount; i++) {
                final GUID curr = Utils.readGUID(source);
                if (GUID.GUID_FILE.equals(curr)) {
                    final ByteArrayOutputStream tmp = new ByteArrayOutputStream();
                    final long size = Utils.readUINT64(source);
                    Utils.writeUINT64(size, tmp);
                    Utils.copy(source, tmp, size - 24);
                    fileHeader = tmp.toByteArray();
                } else {
                    boolean handled = false;
                    for (int j = 0; j < modders.size() && !handled; j++) {
                        if (modders.get(j).isApplicable(curr)) {
                            final ModificationResult result = modders.get(j).modify(curr, source, bos);
                            chunkDiff += result.getChunkCountDifference();
                            totalDiff += result.getByteDifference();
                            modders.remove(j);
                            handled = true;
                        }
                    }
                    if (!handled) {
                        copyChunk(curr, source, bos);
                    }
                }
            }
            for (final ChunkModifier curr : modders) {
                final ModificationResult result = curr.modify(null, null, bos);
                chunkDiff += result.getChunkCountDifference();
                totalDiff += result.getByteDifference();
            }
            dest.write(readGUID.getBytes());
            Utils.writeUINT64(headerSize + totalDiff, dest);
            Utils.writeUINT32(chunkCount + chunkDiff, dest);
            dest.write(reserved);
            modifyFileHeader(new ByteArrayInputStream(fileHeader), dest, totalDiff);
            dest.write(bos.toByteArray());
            Utils.flush(source, dest);
        } else {
            throw new IllegalArgumentException("No ASF header object.");
        }
    }
