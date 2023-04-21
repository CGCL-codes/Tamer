        protected boolean stream(int buffer) {
            try {
                int bytesRead = stream.read(dataBuffer, 0, dataBuffer.capacity());
                if (bytesRead >= 0) {
                    dataBuffer.rewind();
                    int format = stream.getChannels();
                    AL10.alBufferData(buffer, format, dataBuffer, stream.rate());
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
