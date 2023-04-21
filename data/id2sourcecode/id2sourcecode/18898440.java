    public static void audioBufferToStream(AudioFormat f, Buffer buffer, OutputStream os) throws IOException {
        final byte[] data = (byte[]) buffer.getData();
        final int sampleSizeInBytes = f.getSampleSizeInBits() / 8;
        if (sampleSizeInBytes * 8 != f.getSampleSizeInBits()) throw new RuntimeException("Sample size in bytes must be divisible by 8");
        final int frameSizeInBytes = sampleSizeInBytes * f.getChannels();
        final int framesInBuffer = buffer.getLength() / frameSizeInBytes;
        if (buffer.getLength() != framesInBuffer * frameSizeInBytes) throw new RuntimeException("Length of buffer not an integral number of samples");
        final long inputUnsignedMax = (1L << f.getSampleSizeInBits()) - 1;
        final long inputSignedMax = (1L << (f.getSampleSizeInBits() - 1)) - 1;
        for (int frame = 0; frame < framesInBuffer; ++frame) {
            for (int channel = 0; channel < f.getChannels(); ++channel) {
                final int offset = buffer.getOffset() + frame * frameSizeInBytes + channel * sampleSizeInBytes;
                final int inputSampleLiteral = getSample(data, offset, sampleSizeInBytes, f.getEndian());
                final long inputSampleLongWithoutSign = UnsignedUtils.uIntToLong(inputSampleLiteral);
                final long inputSampleLongWithSign;
                if (f.getSigned() == AudioFormat.UNSIGNED) inputSampleLongWithSign = inputSampleLongWithoutSign; else if (f.getSigned() == AudioFormat.SIGNED) {
                    if (inputSampleLongWithoutSign > inputSignedMax) inputSampleLongWithSign = inputSampleLongWithoutSign - inputUnsignedMax - 1; else inputSampleLongWithSign = inputSampleLongWithoutSign;
                } else throw new RuntimeException("input format signed not specified");
                if (channel > 0) os.write(",".getBytes());
                os.write(("" + inputSampleLongWithSign).getBytes());
            }
            os.write("\n".getBytes());
        }
    }
