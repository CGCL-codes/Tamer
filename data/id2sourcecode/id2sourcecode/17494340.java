        @Override
        public boolean validateReader(int readerID) throws IOException {
            testInitialized();
            outOutput.writeInt(LibrarySocketServer.INVOKE_VALIDATE_READER);
            outOutput.writeInt(readerID);
            outOutput.flush();
            boolean valide = outInput.readBoolean();
            return valide;
        }
