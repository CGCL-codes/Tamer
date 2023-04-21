    public void load(String filename) {
        FHimageIDLength = 0;
        FHcolorMapType = 0;
        FHimageType = 0;
        FHcolorMapOrigin = 0;
        FHcolorMapLength = 0;
        FHcolorMapDepth = 0;
        FHimageXOrigin = 0;
        FHimageYOrigin = 0;
        FHwidth = 0;
        FHheight = 0;
        FHbitCount = 0;
        FHimageDescriptor = 0;
        filePointer = 0;
        InputStream dis = ClassLoader.getSystemResourceAsStream(filename);
        try {
            if (dis == null) dis = new FileInputStream(filename);
            fileContents = new byte[dis.available()];
            dis.read(fileContents);
            try {
                dis.close();
            } catch (Exception x) {
            }
            FHimageIDLength = (byte) readUnsignedByte();
            FHcolorMapType = (byte) readUnsignedByte();
            FHimageType = (byte) readUnsignedByte();
            FHcolorMapOrigin = readShort();
            FHcolorMapLength = readShort();
            FHcolorMapDepth = (byte) readUnsignedByte();
            FHimageXOrigin = readShort();
            FHimageYOrigin = readShort();
            FHwidth = readShort();
            FHheight = readShort();
            FHbitCount = (byte) readUnsignedByte();
            FHimageDescriptor = (byte) readUnsignedByte();
            if (FHimageType != 2 && FHimageType != 3) {
                if (FHimageType == 10) loadCompressed();
                fileContents = null;
                return;
            }
            int bytesPerPixel = (FHbitCount / 8);
            data = new byte[FHwidth * FHheight * bytesPerPixel];
            System.arraycopy(fileContents, filePointer, data, 0, data.length);
            if (FHbitCount == 24 || FHbitCount == 32) {
                for (int loop = 0; loop < data.length; loop += bytesPerPixel) {
                    byte btemp = data[loop];
                    data[loop] = data[loop + 2];
                    data[loop + 2] = btemp;
                }
            }
            fileContents = null;
        } catch (Exception x) {
            x.printStackTrace();
            System.out.println(x.getMessage());
        }
    }
