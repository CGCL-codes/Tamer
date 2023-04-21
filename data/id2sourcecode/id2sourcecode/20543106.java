    protected final void writeLiteralString(CharacterSequence str, final int length, int lengthOffset, int simpleType, OutputStream ostream) throws IOException {
        final int n_chars, escapeIndex, startIndex, width;
        final int[] rcs;
        if (simpleType > 0) {
            n_chars = m_schema.getRestrictedCharacterCountOfSimpleType(simpleType);
            if (n_chars != 0) {
                rcs = m_schema.getNodes();
                startIndex = m_schema.getRestrictedCharacterOfSimpleType(simpleType);
                width = BuiltinRCS.WIDTHS[n_chars];
                escapeIndex = startIndex + n_chars;
            } else {
                startIndex = width = escapeIndex = -1;
                rcs = null;
            }
        } else if (simpleType != EXISchema.NIL_NODE) {
            startIndex = 0;
            switch(simpleType) {
                case BuiltinRCS.RCS_ID_BASE64BINARY:
                    rcs = BuiltinRCS.RCS_BASE64BINARY;
                    width = BuiltinRCS.RCS_BASE64BINARY_WIDTH;
                    break;
                case BuiltinRCS.RCS_ID_HEXBINARY:
                    rcs = BuiltinRCS.RCS_HEXBINARY;
                    width = BuiltinRCS.RCS_HEXBINARY_WIDTH;
                    break;
                case BuiltinRCS.RCS_ID_BOOLEAN:
                    rcs = BuiltinRCS.RCS_BOOLEAN;
                    width = BuiltinRCS.RCS_BOOLEAN_WIDTH;
                    break;
                case BuiltinRCS.RCS_ID_DATETIME:
                    rcs = BuiltinRCS.RCS_DATETIME;
                    width = BuiltinRCS.RCS_DATETIME_WIDTH;
                    break;
                case BuiltinRCS.RCS_ID_DECIMAL:
                    rcs = BuiltinRCS.RCS_DECIMAL;
                    width = BuiltinRCS.RCS_DECIMAL_WIDTH;
                    break;
                case BuiltinRCS.RCS_ID_DOUBLE:
                    rcs = BuiltinRCS.RCS_DOUBLE;
                    width = BuiltinRCS.RCS_DOUBLE_WIDTH;
                    break;
                case BuiltinRCS.RCS_ID_INTEGER:
                    rcs = BuiltinRCS.RCS_INTEGER;
                    width = BuiltinRCS.RCS_INTEGER_WIDTH;
                    break;
                default:
                    assert false;
                    width = -1;
                    rcs = null;
                    break;
            }
            escapeIndex = n_chars = rcs.length;
        } else {
            n_chars = startIndex = width = escapeIndex = -1;
            rcs = null;
        }
        final int n_ucsCount = str.getUCSCount();
        writeUnsignedInteger32(lengthOffset + n_ucsCount, ostream);
        final char[] characters = str.getCharacters();
        final int charactersIndex = str.getStartIndex();
        iloop: for (int i = 0; i < length; i++) {
            final int c = characters[charactersIndex + i];
            if (width > 0) {
                int min = startIndex;
                int max = escapeIndex - 1;
                do {
                    final int watershed = (min + max) / 2;
                    final int watershedValue = rcs[watershed];
                    if (c == watershedValue) {
                        writeNBitUnsigned(watershed - startIndex, width, ostream);
                        continue iloop;
                    }
                    if (c < watershedValue) max = watershed - 1; else min = watershed + 1;
                } while (min <= max);
                writeNBitUnsigned(escapeIndex - startIndex, width, ostream);
            }
            final int ucs;
            if ((c & 0xFC00) != 0xD800) ucs = c; else {
                final char c2 = characters[charactersIndex + ++i];
                if ((c2 & 0xFC00) == 0xDC00) {
                    ucs = (((c & 0x3FF) << 10) | (c2 & 0x3FF)) + 0x10000;
                } else {
                    --i;
                    ucs = c;
                }
            }
            writeUnsignedInteger32(ucs, ostream);
        }
    }
