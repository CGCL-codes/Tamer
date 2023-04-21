    public final void read(final File file, long start, long nItems, final long fpos) throws IOException {
        final FileChannel channel = new FileInputStream(file).getChannel();
        if (fpos >= 0) channel.position(BYTESPERELEMENT * fpos);
        final IntBuffer ib = _bb.asIntBuffer();
        if (nItems < 0) nItems = (channel.size() - channel.position()) / BYTESPERELEMENT;
        while (nItems > 0) {
            int bytestoread = (int) ((nItems * BYTESPERELEMENT < BUFSIZE) ? nItems * BYTESPERELEMENT : BUFSIZE);
            final int itemstoread = bytestoread / BYTESPERELEMENT;
            _bb.position(0).limit(bytestoread);
            while ((bytestoread -= channel.read(_bb)) > 0) {
            }
            ib.position(0).limit(itemstoread);
            final int movi = (int) (start / BINSIZE);
            final int movj = (int) (start % BINSIZE);
            final int movs = arrays[movi].length - movj;
            final int tomove = (itemstoread > movs) ? movs : itemstoread;
            ib.get(arrays[movi], movj, tomove);
            if (tomove < itemstoread) ib.get(arrays[movi + 1], 0, itemstoread - tomove);
            start += itemstoread;
            nItems -= itemstoread;
        }
        channel.close();
    }
