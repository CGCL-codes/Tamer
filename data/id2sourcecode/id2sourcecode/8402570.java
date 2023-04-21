    private static void readIncludedFiles(GmFileContext c) throws IOException, GmFormatException {
        GmFile f = c.f;
        GmStreamDecoder in = c.in;
        int ver = in.read4();
        if (ver != 430 && ver != 600 && ver != 620 && ver != 800) throw versionError(f, "BEFORE", "GMI", ver);
        int noIncludes = in.read4();
        for (int i = 0; i < noIncludes; i++) {
            if (ver == 800) {
                in.beginInflate();
                in.skip(8);
            }
            ver = in.read4();
            if (ver != 620 && ver != 800) throw new GmFormatException(f, Messages.format("GmFileReader.ERROR_UNSUPPORTED", Messages.getString("GmFileReader.ININCLUDEDFILES"), ver));
            Include inc = new Include();
            f.includes.add(inc);
            inc.filename = in.readStr();
            inc.filepath = in.readStr();
            inc.isOriginal = in.readBool();
            inc.size = in.read4();
            if (in.readBool()) {
                int s = in.read4();
                inc.data = new byte[s];
                in.read(inc.data, 0, s);
            }
            inc.export = in.read4();
            inc.exportFolder = in.readStr();
            inc.overwriteExisting = in.readBool();
            inc.freeMemAfterExport = in.readBool();
            inc.removeAtGameEnd = in.readBool();
            in.endInflate();
        }
    }
