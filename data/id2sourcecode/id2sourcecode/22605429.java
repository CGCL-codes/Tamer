    public void fillEndOfRecord(int nNbRecordByteAlreadyFilled, int nRecordTotalLength, char cFillerConstant) {
        int nNbBytesToFill = nRecordTotalLength - nNbRecordByteAlreadyFilled;
        m_varDef.writeRepeatingcharAtOffsetWithLength(m_bufferPos, nNbRecordByteAlreadyFilled, cFillerConstant, nNbBytesToFill);
    }
