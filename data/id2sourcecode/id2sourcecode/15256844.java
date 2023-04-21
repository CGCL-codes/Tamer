    public void updateValue() {
        int numPixels = screenwidth * screenheight;
        int origAddress = address.address;
        if (mode == MODE_BINARY) {
            eightColorValues = null;
            trueColorValues = null;
            binColorValues = new boolean[screenwidth][screenheight];
            polys = new PolygonObject[screenwidth][screenheight];
            int bytes = (numPixels + 7) / 8;
            int x = 0, y = 0;
            for (int i = 0; i < bytes; i++) {
                short currentByte = getByte(address);
                Address currentAddress = new Address(address.type, address.size, address.address);
                for (int j = 0; j < 8; j++) {
                    if ((x < screenwidth) && (y < screenheight)) {
                        binColorValues[x][y] = (((currentByte >> j) & 1) == 1);
                        polys[x][y] = new PolygonObject(currentAddress, 1 << j);
                        x++;
                        if (x >= screenwidth) {
                            x = 0;
                            y++;
                        }
                    }
                }
                address.address++;
            }
        } else if (mode == MODE_8COLOR) {
            binColorValues = null;
            trueColorValues = null;
            eightColorValues = new boolean[screenwidth][screenheight][3];
            int bytes = (numPixels + 1) / 2;
            int x = 0, y = 0;
            for (int i = 0; i < bytes; i++) {
                short currentByte = getByte(address);
                for (int j = 0; j < 2; j++) {
                    if ((x < screenwidth) && (y < screenheight)) {
                        for (int k = 0; k < 3; k++) {
                            eightColorValues[x][y][k] = (((currentByte >> (j * 4 + k)) & 1) == 1);
                        }
                        x++;
                        if (x >= screenwidth) {
                            x = 0;
                            y++;
                        }
                    }
                }
                address.address++;
            }
        } else if (mode == MODE_TRUECOLOR) {
            binColorValues = null;
            eightColorValues = null;
            trueColorValues = new short[screenwidth][screenheight][3];
            for (int y = 0; y < screenheight; y++) {
                for (int x = 0; x < screenwidth; x++) {
                    for (int k = 0; k < 3; k++) {
                        short currentByte = getByte(address);
                        trueColorValues[x][y][k] = currentByte;
                        address.address++;
                    }
                    address.address++;
                }
            }
        }
        address.address = origAddress;
    }
