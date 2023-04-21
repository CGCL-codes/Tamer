        private void doEAP(Socket socket, BufferedReader reader, BufferedWriter writer) throws Exception {
            writeBytes(writer, eapResponse(EAP_IDENTITY, (byte) 0, getUsername().getBytes()));
            byte[] reply = readBytes(reader);
            while (reply != null) {
                byte[] send = doEAP(reply);
                writeBytes(writer, send);
                reply = readBytes(reader);
            }
        }
