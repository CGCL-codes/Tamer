    private static void readFonts(GmFileContext c) throws IOException, GmFormatException {
        GmFile f = c.f;
        GmStreamDecoder in = c.in;
        int ver = in.read4();
        if (ver != 440 && ver != 540 && ver != 800) throw versionError(f, "BEFORE", "FNT", (int) in.getPos());
        if (ver == 440) {
            int noDataFiles = in.read4();
            for (int i = 0; i < noDataFiles; i++) {
                if (!in.readBool()) continue;
                in.skip(in.read4());
                if (in.read4() != 440) throw new GmFormatException(f, Messages.format("GmFileReader.ERROR_UNSUPPORTED", Messages.getString("GmFileReader.INDATAFILES"), ver));
                Include inc = new Include();
                f.includes.add(inc);
                inc.filepath = in.readStr();
                inc.filename = new File(inc.filepath).getName();
                if (in.readBool()) {
                    inc.size = in.read4();
                    inc.data = new byte[inc.size];
                    in.read(inc.data, 0, inc.size);
                }
                inc.export = in.read4();
                inc.overwriteExisting = in.readBool();
                inc.freeMemAfterExport = in.readBool();
                inc.removeAtGameEnd = in.readBool();
            }
            return;
        }
        int noFonts = in.read4();
        for (int i = 0; i < noFonts; i++) {
            if (ver == 800) in.beginInflate();
            if (!in.readBool()) {
                f.resMap.getList(Font.class).lastId++;
                in.endInflate();
                continue;
            }
            Font font = f.resMap.getList(Font.class).add();
            font.setName(in.readStr());
            if (ver == 800) in.skip(8);
            ver = in.read4();
            if (ver != 540 && ver != 800) throw versionError(f, "IN", "FNT", i, ver);
            font.put(PFont.FONT_NAME, in.readStr());
            font.put(PFont.SIZE, in.read4());
            in.readBool(font.properties, PFont.BOLD, PFont.ITALIC);
            font.put(PFont.RANGE_MIN, in.read2());
            font.put(PFont.CHARSET, in.read());
            int aa = in.read();
            if (aa == 0 && f.format != GmFile.FormatFlavor.GM_810) aa = 3;
            font.put(PFont.ANTIALIAS, aa);
            font.put(PFont.RANGE_MAX, in.read4());
            in.endInflate();
        }
    }
