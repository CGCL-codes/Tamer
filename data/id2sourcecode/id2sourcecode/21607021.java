    public static void loadBinary(InputStream inStream) throws IOException {
        DataInputStream in = new DataInputStream(inStream);
        head = readShortTable(in, HEAD_LENGTH);
        int[] tableSizes = new int[INDEX_TABLEEND];
        for (int i = 0; i < INDEX_TABLEEND; i++) {
            tableSizes[i] = head[i + 1] - head[i];
        }
        table_scriptIDs = readShortTable(in, tableSizes[INDEX_scriptIDs]);
        table_scriptFonts = readShortTable(in, tableSizes[INDEX_scriptFonts]);
        table_elcIDs = readShortTable(in, tableSizes[INDEX_elcIDs]);
        table_sequences = readShortTable(in, tableSizes[INDEX_sequences]);
        table_fontfileNameIDs = readShortTable(in, tableSizes[INDEX_fontfileNameIDs]);
        table_componentFontNameIDs = readShortTable(in, tableSizes[INDEX_componentFontNameIDs]);
        table_filenames = readShortTable(in, tableSizes[INDEX_filenames]);
        table_awtfontpaths = readShortTable(in, tableSizes[INDEX_awtfontpaths]);
        table_exclusions = readShortTable(in, tableSizes[INDEX_exclusions]);
        table_proportionals = readShortTable(in, tableSizes[INDEX_proportionals]);
        table_scriptFontsMotif = readShortTable(in, tableSizes[INDEX_scriptFontsMotif]);
        table_alphabeticSuffix = readShortTable(in, tableSizes[INDEX_alphabeticSuffix]);
        table_stringIDs = readShortTable(in, tableSizes[INDEX_stringIDs]);
        stringCache = new String[table_stringIDs.length + 1];
        int len = tableSizes[INDEX_stringTable];
        byte[] bb = new byte[len * 2];
        table_stringTable = new char[len];
        in.read(bb);
        int i = 0, j = 0;
        while (i < len) {
            table_stringTable[i++] = (char) (bb[j++] << 8 | (bb[j++] & 0xff));
        }
        if (verbose) {
            dump();
        }
    }
