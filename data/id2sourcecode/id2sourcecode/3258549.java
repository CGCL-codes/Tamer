        @Override
        public byte[] getBytes() {
            int len = 45 + serverVersion.length();
            Buffer buf = new Buffer(len);
            buf.writeByte(protocalVersion);
            buf.writeNullString(serverVersion);
            buf.writeUInt32(threadId);
            buf.writeBytes(scrambleBuff);
            buf.writeByte(filler1);
            buf.writeUInt16(serverCapabilities);
            buf.writeByte(serverLang);
            buf.writeUInt16(serverStatus);
            buf.writeBytes(filler2);
            buf.writeBytes(lastScrambleBuff);
            return buf.getBytes();
        }
