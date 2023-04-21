    private void fillEndOfRecordForLength(int nNbRecordByteAlreadyFilled, int nNbBytesToFill, char cFillerConstant) {
        m_varDef.writeRepeatingcharAtOffsetWithLength(m_bufferPos, nNbRecordByteAlreadyFilled, cFillerConstant, nNbBytesToFill);
    }
