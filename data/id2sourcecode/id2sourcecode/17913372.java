    void finish() throws IOException {
        assert docState.testPoint("TermVectorsTermsWriterPerField.finish start");
        final int numPostings = termsHashPerField.numPostings;
        assert numPostings >= 0;
        if (!doVectors || numPostings == 0) return;
        if (numPostings > maxNumPostings) maxNumPostings = numPostings;
        final IndexOutput tvf = perThread.doc.tvf;
        assert fieldInfo.storeTermVector;
        assert perThread.vectorFieldsInOrder(fieldInfo);
        perThread.doc.addField(termsHashPerField.fieldInfo.number);
        final RawPostingList[] postings = termsHashPerField.sortPostings();
        tvf.writeVInt(numPostings);
        byte bits = 0x0;
        if (doVectorPositions) bits |= TermVectorsReader.STORE_POSITIONS_WITH_TERMVECTOR;
        if (doVectorOffsets) bits |= TermVectorsReader.STORE_OFFSET_WITH_TERMVECTOR;
        tvf.writeByte(bits);
        int encoderUpto = 0;
        int lastTermBytesCount = 0;
        final ByteSliceReader reader = perThread.vectorSliceReader;
        final char[][] charBuffers = perThread.termsHashPerThread.charPool.buffers;
        for (int j = 0; j < numPostings; j++) {
            final TermVectorsTermsWriter.PostingList posting = (TermVectorsTermsWriter.PostingList) postings[j];
            final int freq = posting.freq;
            final char[] text2 = charBuffers[posting.textStart >> DocumentsWriter.CHAR_BLOCK_SHIFT];
            final int start2 = posting.textStart & DocumentsWriter.CHAR_BLOCK_MASK;
            final UnicodeUtil.UTF8Result utf8Result = perThread.utf8Results[encoderUpto];
            UnicodeUtil.UTF16toUTF8(text2, start2, utf8Result);
            final int termBytesCount = utf8Result.length;
            int prefix = 0;
            if (j > 0) {
                final byte[] lastTermBytes = perThread.utf8Results[1 - encoderUpto].result;
                final byte[] termBytes = perThread.utf8Results[encoderUpto].result;
                while (prefix < lastTermBytesCount && prefix < termBytesCount) {
                    if (lastTermBytes[prefix] != termBytes[prefix]) break;
                    prefix++;
                }
            }
            encoderUpto = 1 - encoderUpto;
            lastTermBytesCount = termBytesCount;
            final int suffix = termBytesCount - prefix;
            tvf.writeVInt(prefix);
            tvf.writeVInt(suffix);
            tvf.writeBytes(utf8Result.result, prefix, suffix);
            tvf.writeVInt(freq);
            if (doVectorPositions) {
                termsHashPerField.initReader(reader, posting, 0);
                reader.writeTo(tvf);
            }
            if (doVectorOffsets) {
                termsHashPerField.initReader(reader, posting, 1);
                reader.writeTo(tvf);
            }
        }
        termsHashPerField.reset();
        perThread.termsHashPerThread.reset(false);
    }
