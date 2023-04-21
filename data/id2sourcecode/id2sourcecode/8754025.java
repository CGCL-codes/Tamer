    private void readPacket() throws IOException {
        number = in.readUnsignedShort();
        if (number != -1) {
            length = in.readShort();
            if (length > 0) {
                copied = 0;
                readed = 0;
                remaining = length;
                while (copied < length) {
                    readed = in.read(packetBuffer, copied, remaining);
                    if (readed != -1) {
                        pipedOutputStreams[number].write(packetBuffer, copied, readed);
                        pipedOutputStreams[number].flush();
                        copied += readed;
                        remaining -= readed;
                    } else {
                        close();
                        break;
                    }
                }
            } else if (length == -1) {
                close();
            } else if (length == -2) {
                close(number);
            } else if (length == -3) {
                open(number);
            }
        } else {
            close();
        }
    }
