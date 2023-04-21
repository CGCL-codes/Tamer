    public final NtpResult poll() throws IOException {
        final DatagramChannel channel = getChannel();
        final byte[] a = NtpPacket.createRequest();
        final ByteBuffer buffer = ByteBuffer.wrap(a);
        final NtpTimestamp tsSend = NtpTimestamp.now();
        Longs.toBytes(tsSend.ntp, a, NtpPacket.TRANSMIT_TIMESTAMP_INDEX);
        channel.write(buffer);
        buffer.clear();
        channel.read(buffer);
        final NtpTimestamp tsReceive = NtpTimestamp.now();
        if (buffer.hasRemaining()) throw new ProtocolException("received incomplete packet, expected 48 bytes, got " + buffer.position());
        return new NtpResult(new NtpPacket(a), tsSend, tsReceive);
    }
