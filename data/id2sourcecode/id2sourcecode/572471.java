    public void setHDMAControl(byte control) {
        boolean mode = (control & 0x80) == 0x80;
        int i, length = ((control & 0x7F) + 1) * 0x10;
        hdmaSrc = ((rHDMA1 & 0xFF) << 8) | (rHDMA2 & 0xF0);
        hdmaDst = ((rHDMA3 & 0x1F) << 8) | (rHDMA4 & 0xF0) + 0x8000;
        if (mode) {
            hdmaDone = false;
            hdmaStop = hdmaDst + length;
        } else {
            for (i = 0; i < length; i++) {
                write(hdmaDst++, read(hdmaSrc++));
            }
        }
    }
