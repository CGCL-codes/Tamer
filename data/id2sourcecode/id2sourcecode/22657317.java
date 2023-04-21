    public int process(Buffer inputBuffer, Buffer outputBuffer) {
        if (isEOM(inputBuffer)) {
            propagateEOM(outputBuffer);
            return BUFFER_PROCESSED_OK;
        }
        byte[] inBuffer = (byte[]) inputBuffer.getData();
        int inLength = inputBuffer.getLength();
        int inOffset = inputBuffer.getOffset();
        int samplesNumber = inLength;
        AudioFormat af = (AudioFormat) inputBuffer.getFormat();
        if (enabled) {
            int shiftZero = 0;
            int shiftOne = 8;
            if (af.getEndian() == AudioFormat.BIG_ENDIAN) {
                shiftZero = 8;
                shiftOne = 0;
            }
            int spa = ((int) af.getSampleRate() * af.getChannels()) / nPowersPerSec;
            long npa = 1000000000L / nPowersPerSec;
            long timeStamp = inputBuffer.getTimeStamp();
            float average = 0;
            long cspa = 0;
            for (int i = 0; i < inLength; i += 2) {
                short sample = (short) (((0xFF & inBuffer[inOffset + i]) << shiftZero) | ((0xFF & inBuffer[inOffset + i + 1]) << shiftOne));
                float normal = (float) sample;
                average = average + normal * normal;
                cspa++;
                if (cspa == spa) {
                    cspa = 0;
                    average = (float) Math.sqrt((average / spa)) / 32768;
                    push(timeStamp, average);
                    timeStamp += npa;
                    average = 0;
                }
            }
        }
        inputBuffer.setData(outputBuffer.getData());
        outputBuffer.setFormat(af);
        outputBuffer.setData(inBuffer);
        outputBuffer.setLength(inLength);
        outputBuffer.setOffset(inOffset);
        outputBuffer.setTimeStamp(inputBuffer.getTimeStamp());
        outputBuffer.setFlags(inputBuffer.getFlags());
        return BUFFER_PROCESSED_OK;
    }
