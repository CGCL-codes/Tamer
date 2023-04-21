        public Buffer8PlusInputStream() {
            framesize_pc = format.getFrameSize() / format.getChannels();
            bigendian = format.isBigEndian();
        }
