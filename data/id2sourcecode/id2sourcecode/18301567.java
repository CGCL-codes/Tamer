    private void writeHeaderFrames() {
        int length = comment.length();
        if (length > 247) {
            comment = comment.substring(0, 247);
            length = 247;
        }
        while ((buf.length - count) < length + 144) {
            int nsz = buf.length * 2;
            byte[] nbuf = new byte[nsz];
            System.arraycopy(buf, 0, nbuf, 0, count);
            buf = nbuf;
        }
        AudioFileWriter.writeOggPageHeader(buf, count, 2, granulepos, streamSerialNumber, pageCount++, 1, new byte[] { 80 });
        oggCount = count + 28;
        AudioFileWriter.writeSpeexHeader(buf, oggCount, encoder.getSampleRate(), mode, encoder.getChannels(), encoder.getEncoder().getVbr(), framesPerPacket);
        oggCount += 80;
        int chksum = OggCrc.checksum(0, buf, count, oggCount - count);
        AudioFileWriter.writeInt(buf, count + 22, chksum);
        count = oggCount;
        AudioFileWriter.writeOggPageHeader(buf, count, 0, granulepos, streamSerialNumber, pageCount++, 1, new byte[] { (byte) (length + 8) });
        oggCount = count + 28;
        AudioFileWriter.writeSpeexComment(buf, oggCount, comment);
        oggCount += length + 8;
        chksum = OggCrc.checksum(0, buf, count, oggCount - count);
        AudioFileWriter.writeInt(buf, count + 22, chksum);
        count = oggCount;
        packetCount = 0;
    }
