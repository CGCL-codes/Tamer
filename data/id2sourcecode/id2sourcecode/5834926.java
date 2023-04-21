    public static void simpToTrad(Reader reader, Writer writer, boolean lexicalMapping) throws IOException {
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
                int p = STLexemicMapping.findBlock(ch);
                if (p != -1) {
                    int start = STLexemicMapping.blockStart(p);
                    int end = STLexemicMapping.blockEnd(p);
                    for (int i = start; i < end; i++) {
                        String glossary = STLexemicMapping.SC_TO_TC_LEXEME[i];
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
                            writer.write(STLexemicMapping.SC_TO_TC_LEXEME_MAP[i]);
                            in.skip(l - 1);
                            continue continueConvert;
                        }
                    }
                }
            }
            if (Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS.equals(Character.UnicodeBlock.of(ch))) {
                writer.write(STCodeMapping.SC_TO_TC_CODE_MAP[ch - CJK_UNIFIED_IDEOGRAPHS_START]);
            } else {
                writer.write(ch);
            }
        }
    }
