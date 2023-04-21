    public RandomAccessCharacterFile(RandomAccessFile raf, String encoding) throws IOException {
        fcn = raf.getChannel();
        cset = (encoding == null) ? Charset.defaultCharset() : Charset.forName(encoding);
        cdec = cset.newDecoder();
        cdec.onMalformedInput(CodingErrorAction.REPLACE);
        cdec.onUnmappableCharacter(CodingErrorAction.REPLACE);
        cenc = cset.newEncoder();
        bbuf = ByteBuffer.allocate(BUFSIZ);
        bbuf.flip();
        bbufIsDirty = false;
        bbufIsReadable = false;
        bbufpos = fcn.position();
        reader = new RandomAccessReader();
        writer = new RandomAccessWriter();
        inputStream = new RandomAccessInputStream();
        outputStream = new RandomAccessOutputStream();
    }
