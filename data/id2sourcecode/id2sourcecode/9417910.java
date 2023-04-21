    private void init(String inFile, String outFile, float distortion, float ftGainExponent, int verticalBlockSize, int horizontalBlockSize, int verticalTransLength, int horizontalTransLength, int frameKeyOffset, int frameKeyLength, long tracesToDump) throws IOException {
        _frameKeyOffset = frameKeyOffset;
        _frameKeyLength = frameKeyLength;
        _tracesToDump = tracesToDump;
        _inputStream = new FileInputStream(inFile);
        _inputChannel = _inputStream.getChannel();
        if (outFile != null) {
            _outputStream = new FileOutputStream(outFile);
            _outputChannel = _outputStream.getChannel();
        }
        _workBuffer = ByteBuffer.allocate(8);
        _workBuffer.order(ByteOrder.BIG_ENDIAN);
        if (_outputChannel != null) {
            _workBuffer.putInt(0, COOKIE_V1);
            _workBuffer.putInt(4, 0);
            _workBuffer.position(0);
            int nWritten = _outputChannel.write(_workBuffer);
            _nbytesWritten += nWritten;
            if (nWritten != 8) throw new IOException("Error writing file header: " + nWritten + "!=8");
        }
        ByteBuffer reelHdrBuffer = ByteBuffer.allocate(LEN_REEL_HDR);
        int nRead = _inputChannel.read(reelHdrBuffer);
        _nbytesRead += nRead;
        if (nRead != LEN_REEL_HDR) throw new IOException("Error reading SEG-Y reel header: " + nRead + "!=" + LEN_REEL_HDR);
        if (outFile != null) {
            reelHdrBuffer.position(0);
            int nWritten = _outputChannel.write(reelHdrBuffer);
            _nbytesWritten += nWritten;
            if (nWritten != LEN_REEL_HDR) throw new IOException("Error writing SEG-Y reel header: " + nWritten + "!=" + LEN_REEL_HDR);
        }
        ByteBuffer binaryHdrBuffer = ByteBuffer.allocate(LEN_BINARY_HDR);
        nRead = _inputChannel.read(binaryHdrBuffer);
        _nbytesRead += nRead;
        if (nRead != LEN_BINARY_HDR) throw new IOException("Error reading SEG-Y binary header: " + nRead + "!=" + LEN_BINARY_HDR);
        if (outFile != null) {
            binaryHdrBuffer.position(0);
            int nWritten = _outputChannel.write(binaryHdrBuffer);
            _nbytesWritten += nWritten;
            if (nWritten != LEN_BINARY_HDR) throw new IOException("Error writing SEG-Y binary header: " + nWritten + "!=" + LEN_BINARY_HDR);
        }
        binaryHdrBuffer.order(ByteOrder.BIG_ENDIAN);
        int tracesPerFrame = binaryHdrBuffer.getShort(12);
        System.out.println("tracesPerFrame= " + tracesPerFrame);
        float sampleInterval = (float) binaryHdrBuffer.getShort(16) / 1000.0F;
        System.out.println("sampleInterval= " + sampleInterval);
        int samplesPerTrace = (int) binaryHdrBuffer.getShort(20);
        System.out.println("samplesPerTrace= " + samplesPerTrace);
        _nbytesPerTrace = samplesPerTrace * 4;
        _seisPEG = new SeisPEG(samplesPerTrace, tracesPerFrame, distortion, verticalBlockSize, horizontalBlockSize, verticalTransLength, horizontalTransLength);
        if (ftGainExponent != 0.0) _seisPEG.setGainExponent(ftGainExponent);
        _traces = new float[tracesPerFrame][samplesPerTrace];
        _hdrs = new int[tracesPerFrame][NINTS_PER_HDR];
        _compressedHdrBytes = new byte[HdrCompressor.getOutputBufferSize(NINTS_PER_HDR, tracesPerFrame)];
        _compressedHdrByteBuffer = ByteBuffer.wrap(_compressedHdrBytes);
        _compressedTrcBytes = new byte[samplesPerTrace * tracesPerFrame * 4];
        _compressedTrcByteBuffer = ByteBuffer.wrap(_compressedTrcBytes);
    }
