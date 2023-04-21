        private WriteThread(final Device writeDevice, final int writeBlockAddress, final byte[] writeData) {
            device = writeDevice;
            address = writeBlockAddress;
            data = writeData;
        }
