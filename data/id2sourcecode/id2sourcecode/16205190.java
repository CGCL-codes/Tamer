    public void load(String filename) {
        FHsize = 0;
        FHreserved1 = 0;
        FHreserved2 = 0;
        FHoffsetBits = 0;
        IHsize = 0;
        IHwidth = 0;
        IHheight = 0;
        IHplanes = 0;
        IHbitCount = 0;
        IHcompression = 0;
        IHsizeImage = 0;
        IHxpelsPerMeter = 0;
        IHypelsPerMeter = 0;
        IHcolorsUsed = 0;
        IHcolorsImportant = 0;
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
            short magicNumber = readShort();
            if (magicNumber != 19778) {
                fileContents = null;
                return;
            }
            FHsize = readInt();
            FHreserved1 = readShort();
            FHreserved2 = readShort();
            FHoffsetBits = readInt();
            IHsize = readInt();
            IHwidth = readInt();
            IHheight = readInt();
            IHplanes = readShort();
            IHbitCount = readShort();
            IHcompression = readInt();
            IHsizeImage = readInt();
            IHxpelsPerMeter = readInt();
            IHypelsPerMeter = readInt();
            IHcolorsUsed = readInt();
            IHcolorsImportant = readInt();
            data = new byte[IHsizeImage];
            System.arraycopy(fileContents, FHoffsetBits, data, 0, IHsizeImage);
            fileContents = null;
            for (int loop = 0; loop < IHsizeImage; loop += 3) {
                byte btemp = data[loop];
                data[loop] = data[loop + 2];
                data[loop + 2] = btemp;
            }
        } catch (Exception x) {
            x.printStackTrace();
            System.out.println(x.getMessage());
        }
    }
