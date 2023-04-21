    FileLoader(final Context ctxt, final RandomAccessInputStream inputStream) throws IOException, InvalidFileFormatException, NoSuchAlgorithmException, Fatal.Exception {
        assert ctxt != null : "null ctxt";
        assert inputStream != null : "null inputStream";
        final long length = inputStream.length();
        DataInput in = inputStream.dataInputFrom(length - FileLoader.TRAILER_SIZE);
        final int nbSections = Misc.ensure32s(IO.read32u(in));
        final byte[] magic = new byte[FileLoader.EXEC_MAGIC.length()];
        in.readFully(magic);
        if (!FileLoader.EXEC_MAGIC.equals(Misc.convertBytesToString(magic))) {
            throw new InvalidFileFormatException(FileLoader.INVALID_MAGIC);
        }
        long offset = 0;
        in = inputStream.dataInputFrom(length - (FileLoader.TRAILER_SIZE + FileLoader.SECTION_SIZE * nbSections));
        final Map<String, Section> sections = new LinkedHashMap<String, Section>();
        for (int i = 0; i < nbSections; i++) {
            final byte[] tmpName = new byte[FileLoader.SECTION_NAME_LENGTH];
            in.readFully(tmpName);
            final String name = Misc.convertBytesToString(tmpName);
            final long lenSection = IO.read32u(in);
            if (lenSection > Integer.MAX_VALUE) {
                throw new InvalidFileFormatException(FileLoader.INVALID_SECTION);
            }
            sections.put(name, new Section(name, offset, (int) lenSection));
            offset += lenSection;
        }
        for (Section s : sections.values()) {
            s.offset = length - (offset - s.offset + FileLoader.TRAILER_SIZE + FileLoader.SECTION_SIZE * nbSections);
        }
        final Section codeSection = sections.get(FileLoader.SECTION_CODE);
        if (codeSection == null) {
            throw new InvalidFileFormatException(String.format(FileLoader.MISSING_SECTION, FileLoader.SECTION_CODE));
        }
        final int codeSize = codeSection.size;
        if ((codeSize % 4) != 0) {
            throw new InvalidFileFormatException(FileLoader.INVALID_CODE_SECTION_SIZE);
        }
        final byte[] codeData = new byte[codeSize];
        in = inputStream.dataInputFrom(codeSection.offset);
        in.readFully(codeData);
        final MessageDigest md5 = MessageDigest.getInstance(FileLoader.MD5_ALGO);
        final byte[] codeDigest = md5.digest(codeData);
        ctxt.setCodeDigest(codeDigest);
        int ptr = 0;
        while (ptr < codeSize) {
            byte tmp = codeData[ptr];
            codeData[ptr] = codeData[ptr + 3];
            codeData[ptr + 3] = tmp;
            tmp = codeData[ptr + 1];
            codeData[ptr + 1] = codeData[ptr + 2];
            codeData[ptr + 2] = tmp;
            ptr += 4;
        }
        final ByteArrayInputStream bais = new ByteArrayInputStream(codeData);
        final DataInputStream dis = new DataInputStream(bais);
        final int lenCode = codeSize / 4;
        this.code = new int[lenCode];
        for (int i = 0; i < lenCode; i++) {
            this.code[i] = dis.readInt();
        }
        ctxt.appendCode(this.code);
        ctxt.setupCallbackTail();
        this.sharedLibPath = Collections.unmodifiableList(readSection(inputStream, FileLoader.SECTION_SHARED_LIB_PATH, sections, false));
        this.sharedLibs = Collections.unmodifiableList(readSection(inputStream, FileLoader.SECTION_SHARED_LIBS, sections, false));
        final List<String> primList = readSection(inputStream, FileLoader.SECTION_REQ_PRIMS, sections, true);
        if ((primList == null) || (primList.size() == 0)) {
            throw new InvalidFileFormatException(String.format(FileLoader.MISSING_SECTION, FileLoader.SECTION_REQ_PRIMS));
        }
        this.primitives = Collections.unmodifiableList(primList);
        final Section data = sections.get(FileLoader.SECTION_DATA);
        if (data == null) {
            throw new InvalidFileFormatException(String.format(FileLoader.MISSING_SECTION, FileLoader.SECTION_DATA));
        }
        in = inputStream.dataInputFrom(data.offset);
        try {
            this.globalData = Intern.inputVal(ctxt, in, true);
        } catch (final Fail.Exception fe) {
            throw new InvalidFileFormatException(fe.getMessage());
        }
        ctxt.setGlobalData(this.globalData);
        final Section debug = sections.get(FileLoader.SECTION_DEBUG);
        if (debug != null) {
            in = inputStream.dataInputFrom(debug.offset);
            final int numEvents = Misc.ensure32s(IO.read32u(in));
            final Block events = Block.createBlock(numEvents, 0);
            for (int i = 0; i < numEvents; i++) {
                final int orig = Misc.ensure32s(IO.read32u(in));
                final Value evl;
                try {
                    evl = Intern.inputVal(ctxt, in, true);
                } catch (final Fail.Exception fe) {
                    throw new InvalidFileFormatException(fe.toString());
                }
                Value l = evl;
                while (l != Value.EMPTY_LIST) {
                    final Block b = l.asBlock();
                    final Block blk = b.get(0).asBlock();
                    blk.set(BackTrace.EV_POS, Value.createFromLong(blk.get(BackTrace.EV_POS).asLong() + orig));
                    l = b.get(1);
                }
                events.set(i, evl);
            }
            this.debugInfo = Value.createFromBlock(events);
        } else {
            this.debugInfo = Value.FALSE;
        }
        ctxt.setDebugInfo(this.debugInfo);
    }
