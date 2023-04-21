    private int sendChunks(ByteBuffer pkt, int maxChunks, OutputStream out) throws IOException {
        int len = Math.min((int) (endOffset - offset), bytesPerChecksum * maxChunks);
        if (len == 0) {
            return 0;
        }
        int numChunks = (len + bytesPerChecksum - 1) / bytesPerChecksum;
        int packetLen = len + numChunks * checksumSize + 4;
        pkt.clear();
        pkt.putInt(packetLen);
        pkt.putLong(offset);
        pkt.putLong(seqno);
        pkt.put((byte) ((offset + len >= endOffset) ? 1 : 0));
        pkt.putInt(len);
        int checksumOff = pkt.position();
        int checksumLen = numChunks * checksumSize;
        byte[] buf = pkt.array();
        if (checksumSize > 0 && checksumIn != null) {
            try {
                checksumIn.readFully(buf, checksumOff, checksumLen);
            } catch (IOException e) {
                LOG.warn(" Could not read or failed to veirfy checksum for data" + " at offset " + offset + " for block " + block + " got : " + StringUtils.stringifyException(e));
                IOUtils.closeStream(checksumIn);
                checksumIn = null;
                if (corruptChecksumOk) {
                    if (checksumOff < checksumLen) {
                        Arrays.fill(buf, checksumOff, checksumLen, (byte) 0);
                    }
                } else {
                    throw e;
                }
            }
        }
        int dataOff = checksumOff + checksumLen;
        if (blockInPosition < 0) {
            IOUtils.readFully(blockIn, buf, dataOff, len);
            if (verifyChecksum) {
                int dOff = dataOff;
                int cOff = checksumOff;
                int dLeft = len;
                for (int i = 0; i < numChunks; i++) {
                    checksum.reset();
                    int dLen = Math.min(dLeft, bytesPerChecksum);
                    checksum.update(buf, dOff, dLen);
                    if (!checksum.compare(buf, cOff)) {
                        throw new ChecksumException("Checksum failed at " + (offset + len - dLeft), len);
                    }
                    dLeft -= dLen;
                    dOff += dLen;
                    cOff += checksumSize;
                }
            }
        }
        try {
            if (blockInPosition >= 0) {
                SocketOutputStream sockOut = (SocketOutputStream) out;
                sockOut.write(buf, 0, dataOff);
                sockOut.transferToFully(((FileInputStream) blockIn).getChannel(), blockInPosition, len);
                blockInPosition += len;
            } else {
                out.write(buf, 0, dataOff + len);
            }
        } catch (IOException e) {
            throw ioeToSocketException(e);
        }
        if (throttler != null) {
            throttler.throttle(packetLen);
        }
        return len;
    }
