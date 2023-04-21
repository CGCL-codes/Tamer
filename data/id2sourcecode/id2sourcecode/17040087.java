    public int process(Buffer inputBuffer, Buffer outputBuffer) {
        if (!checkInputBuffer(inputBuffer)) {
            return BUFFER_PROCESSED_FAILED;
        }
        if (isEOM(inputBuffer)) {
            propagateEOM(outputBuffer);
            return BUFFER_PROCESSED_OK;
        }
        int channels = ((AudioFormat) outputFormat).getChannels();
        byte[] inData = (byte[]) inputBuffer.getData();
        int inpLength = inputBuffer.getLength();
        int outLength = 0;
        int inOffset = inputBuffer.getOffset();
        int outOffset = outputBuffer.getOffset();
        Format newFormat = inputBuffer.getFormat();
        if (lastFormat != newFormat) {
            initConverter((AudioFormat) newFormat);
        }
        try {
            decoder.processData(inData, inOffset, inpLength);
            outLength = decoder.getProcessedDataByteSize();
            byte[] outData = validateByteArraySize(outputBuffer, outLength);
            decoder.getProcessedData(outData, outOffset);
        } catch (StreamCorruptedException ex) {
            ex.printStackTrace();
        }
        updateOutput(outputBuffer, outputFormat, outLength, 0);
        return BUFFER_PROCESSED_OK;
    }
