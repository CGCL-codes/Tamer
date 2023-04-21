    private void getInfo() {
        byte[] buf;
        if (this.extension.equals("gif")) {
            buf = new byte[10];
            int read = readBytes(buf);
            properties.put("width", new Integer(-1));
            properties.put("height", new Integer(-1));
            if (read == 10) {
                if (unsigned(buf[0]) == 0x47 && unsigned(buf[1]) == 0x49 && unsigned(buf[2]) == 0x46) {
                    properties.put("width", new Integer(unsigned(buf[6]) + (unsigned(buf[7]) << 8)));
                    properties.put("height", new Integer(unsigned(buf[8]) + (unsigned(buf[9]) << 8)));
                }
            }
        } else if (this.extension.equals("jpg") || this.extension.equals("jpeg")) {
            debug.write("checking jpg");
            buf = new byte[64000];
            int read = readBytes(buf);
            debug.write("read " + read + " bytes");
            properties.put("width", new Integer(-1));
            properties.put("height", new Integer(-1));
            if (read > 3) {
                if ((unsigned(buf[0]) == 255) && (unsigned(buf[1]) == 216) && (unsigned(buf[2]) == 255)) {
                    debug.write("Testing for JPEG");
                    int index = 0;
                    boolean bFound = false;
                    for (; (index < read - 8) && !bFound; index++) {
                        if (unsigned(buf[index]) == 255 && validJPG.contains(new Integer(unsigned(buf[index + 1])))) {
                            debug.write("Found it - index = " + index);
                            bFound = true;
                        }
                    }
                    if (bFound) {
                        properties.put("width", new Integer(unsigned(buf[index + 7]) + (unsigned(buf[index + 6]) << 8)));
                        properties.put("height", new Integer(unsigned(buf[index + 5]) + (unsigned(buf[index + 4]) << 8)));
                    }
                }
            }
        }
    }
