        private WriteThread(final Device writeDevice, final long writeBlockAddress, final byte[] writeData) {
            device = writeDevice;
            address = writeBlockAddress;
            data = writeData;
        }
