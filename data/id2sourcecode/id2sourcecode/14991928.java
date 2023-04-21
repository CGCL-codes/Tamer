        private void get() throws IOException, SocketException, NoSuchAlgorithmException {
            DataInputStream dis = null;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] body = null;
            byte[] header = null;
            dis = new DataInputStream(theSocket.getInputStream());
            for (int i = 0; i < 12; i++) {
                baos.write(dis.readByte());
            }
            header = baos.toByteArray();
            baos.reset();
            int Body_Len = Header.extractBodyLen(header);
            for (int i = 0; i < Body_Len; i++) {
                baos.write(dis.readByte());
            }
            byte[] tempBody = baos.toByteArray();
            byte headerVersionNumber = Header.extractVersionNumber(header);
            byte headerFlags = Header.extractFlags(header);
            byte headerSequenceNumber = Header.extractSeqNum(header);
            body = Header.crypt(headerVersionNumber, headerSequenceNumber, tempBody, headerFlags, sessionID, secretkey);
            REPLY_status = body[0];
            REPLY_flags = body[1];
        }
