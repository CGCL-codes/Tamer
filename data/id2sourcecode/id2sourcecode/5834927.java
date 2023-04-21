    public static void tradToSimp(Reader reader, Writer writer, boolean lexicalMapping) throws IOException {
        Reader in = null;
        if (!reader.markSupported()) {
            in = new BufferedReader(reader);
        } else {
            in = reader;
        }
        int c;
        continueConvert: while ((c = in.read()) != -1) {
            char ch = (char) c;
            if (lexicalMapping) {
                int p = TSLexemicMapping.findBlock(ch);
                if (p != -1) {
                    int start = TSLexemicMapping.blockStart(p);
                    int end = TSLexemicMapping.blockEnd(p);
                    for (int i = start; i < end; i++) {
                        String glossary = TSLexemicMapping.TC_TO_SC_LEXEME[i];
                        int l = glossary.length();
                        in.mark(READ_AHEAD_LIMIT);
                        boolean match = true;
                        for (int j = 1; j < l; j++) {
                            if (in.read() != glossary.charAt(j)) {
                                match = false;
                                break;
                            }
                        }
                        in.reset();
                        if (match) {
                            writer.write(TSLexemicMapping.TC_TO_SC_LEXEME_MAP[i]);
                            in.skip(l - 1);
                            continue continueConvert;
                        }
                    }
                }
            }
            if (Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS.equals(Character.UnicodeBlock.of(ch))) {
                writer.write(TSCodeMapping.TC_TO_SC_CODE_MAP[ch - CJK_UNIFIED_IDEOGRAPHS_START]);
            } else {
                writer.write(ch);
            }
        }
    }
