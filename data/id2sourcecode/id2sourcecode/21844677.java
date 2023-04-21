    private int getHardwareValue() throws IOException {
        byte[] data = new byte[2];
        if (getChannel() <= 256) {
            data[0] = 0x52;
            data[1] = (byte) (getChannel() - 1);
        } else if (getChannel() <= 512) {
            data[0] = 0x53;
            data[1] = (byte) (getChannel() - 1);
        }
        getController().getOutputStream().write(data);
        byte[] response = new byte[3];
        if (getController().getInputStream().read(response) == 3) return response[2] & 0xff; else return value;
    }
