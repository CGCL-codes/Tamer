    public static void main(String[] args) {
        File tempFile = null;
        try {
            tempFile = createTempFile();
            RandomAccessFile raf = new RandomAccessFile(tempFile, "rw");
            raf.seek(0);
            raf.seek(0);
            ASCIIChar a = new ASCIIChar();
            System.out.print("Testing ASCIIChar (" + a.getPrimitiveDescription() + ") ... ");
            a.setValueFromString("a");
            a.writeValueToFile(raf);
            raf.seek(0);
            a.setValueFromFile(raf);
            if (!a.getValue().equals("a")) {
                throw new IOException("ASCIIChar read/write not successful");
            }
            System.out.println("PASSED");
            raf.seek(0);
            FourCC x = new FourCC();
            System.out.print("Testing FOURCC (" + x.getPrimitiveDescription() + ") ... ");
            x.setValueFromString("abCd");
            x.writeValueToFile(raf);
            raf.seek(0);
            x.setValueFromFile(raf);
            if (!x.getValue().equals("abCd")) {
                throw new IOException("FourCC read/write not successful");
            }
            System.out.println("PASSED");
            raf.seek(0);
            GUID guid = new GUID();
            System.out.print("Testing GUID (" + guid.getPrimitiveDescription() + ") ... ");
            guid.setValueFromString("05908040-3246-11d0-a5d6-28db04c10000");
            guid.writeValueToFile(raf);
            raf.seek(0);
            guid.setValueFromFile(raf);
            if (!guid.writeValueToString().equals("05908040-3246-11d0-a5d6-28db04c10000")) {
                throw new IOException("GUID read/write not successful" + guid.writeValueToString());
            }
            System.out.println("PASSED");
            raf.seek(0);
            NullTerminatedString n = new NullTerminatedString("US-ASCII", false);
            System.out.print("Testing NullTerminatedString (" + n.getPrimitiveDescription() + ") ... ");
            n.setValueFromString("Here is the string");
            n.writeValueToFile(raf);
            raf.seek(0);
            n.setValueFromFile(raf);
            if (!n.getValue().equals("Here is the string")) {
                throw new IOException("NullTerminatedString read/write not successful" + n.getValue());
            }
            System.out.println("PASSED");
            raf.seek(0);
            raf.setLength(0);
            PlainString ps = new PlainString("VALUE", "US-ASCII", true);
            System.out.print("Testing PlainString (" + ps.getPrimitiveDescription() + ") ... ");
            ps.writeValueToFile(raf);
            raf.seek(0);
            ps.setValueFromFile(raf);
            if (!ps.getValue().equals("VALUE")) {
                throw new IOException("PlainString read/write not successful" + ps.getValue());
            }
            System.out.println("PASSED");
            raf.seek(0);
            raf.setLength(0);
            CompressedString cs = new CompressedString("VALUE", "US-ASCII", true);
            System.out.print("Testing CompressedString (" + cs.getPrimitiveDescription() + ") ... ");
            cs.writeValueToFile(raf);
            raf.seek(0);
            cs.setValueFromFile(raf);
            if (!cs.getValue().equals("VALUE")) {
                throw new IOException("CompressedString read/write not successful" + cs.getValue());
            }
            System.out.println("PASSED");
            raf.seek(0);
            IEEE80ByteFloat ie = new IEEE80ByteFloat();
            System.out.print("Testing IEEE80ByteFloat (" + ie.getPrimitiveDescription() + ") ... ");
            ie.setValueFromString("44100");
            ie.writeValueToFile(raf);
            raf.seek(0);
            ie.setValueFromFile(raf);
            if (ie.getValue().intValue() != 44100) {
                throw new IOException("IEEE80ByteFloat read/write not successful");
            }
            System.out.println("PASSED");
            raf.seek(0);
            Int2ByteSignedBE xx = new Int2ByteSignedBE();
            System.out.print("Testing Int2ByteSignedBE (" + xx.getPrimitiveDescription() + ") ... ");
            xx.setValueFromString("-257");
            xx.writeValueToFile(raf);
            raf.seek(0);
            xx.setValueFromFile(raf);
            if (xx.getValue().intValue() != -257) {
                throw new IOException("Int2ByteSignedBE read/write not successful");
            }
            System.out.println("PASSED");
            raf.seek(0);
            Int2ByteSignedLE ab = new Int2ByteSignedLE();
            System.out.print("Testing Int2ByteSignedLE (" + ab.getPrimitiveDescription() + ") ... ");
            ab.setValueFromString("-257");
            ab.writeValueToFile(raf);
            raf.seek(0);
            ab.setValueFromFile(raf);
            if (ab.getValue().intValue() != -257) {
                throw new IOException("Int2ByteSignedLE read/write not successful");
            }
            System.out.println("PASSED");
            raf.seek(0);
            Int2ByteUnsignedBE abv = new Int2ByteUnsignedBE();
            System.out.print("Testing Int2ByteUnsignedBE (" + abv.getPrimitiveDescription() + ") ... ");
            abv.setValueFromString("257");
            abv.writeValueToFile(raf);
            raf.seek(0);
            abv.setValueFromFile(raf);
            if (abv.getValue().intValue() != 257) {
                throw new IOException("Int2ByteUnsignedBE read/write not successful");
            }
            System.out.println("PASSED");
            raf.seek(0);
            Int2ByteUnsignedLE abw = new Int2ByteUnsignedLE();
            System.out.print("Testing Int2ByteUnsignedLE (" + abw.getPrimitiveDescription() + ") ... ");
            abw.setValueFromString("257");
            abw.writeValueToFile(raf);
            raf.seek(0);
            abw.setValueFromFile(raf);
            if (abw.getValue().intValue() != 257) {
                throw new IOException("Int2ByteUnsignedLE read/write not successful");
            }
            System.out.println("PASSED");
            raf.seek(0);
            Int4ByteSignedBE xxq = new Int4ByteSignedBE();
            System.out.print("Testing Int4ByteSignedBE (" + xxq.getPrimitiveDescription() + ") ... ");
            xxq.setValueFromString("-25744");
            xxq.writeValueToFile(raf);
            raf.seek(0);
            xxq.setValueFromFile(raf);
            if (xxq.getValue().intValue() != -25744) {
                throw new IOException("Int4ByteSignedBE read/write not successful");
            }
            System.out.println("PASSED");
            raf.seek(0);
            Int4ByteSignedLE abq = new Int4ByteSignedLE();
            System.out.print("Testing Int4ByteSignedLE (" + abq.getPrimitiveDescription() + ") ... ");
            abq.setValueFromString("-25744");
            abq.writeValueToFile(raf);
            raf.seek(0);
            abq.setValueFromFile(raf);
            if (abq.getValue().longValue() != -25744) {
                throw new IOException("Int4ByteSignedLE read/write not successful");
            }
            System.out.println("PASSED");
            raf.seek(0);
            Int4ByteUnsignedBE abvww = new Int4ByteUnsignedBE();
            System.out.print("Testing Int4ByteUnsignedBE (" + abvww.getPrimitiveDescription() + ") ... ");
            abvww.setValueFromString("25755");
            abvww.writeValueToFile(raf);
            raf.seek(0);
            abvww.setValueFromFile(raf);
            if (abvww.getValue().longValue() != 25755) {
                throw new IOException("Int4ByteUnsignedBE read/write not successful");
            }
            System.out.println("PASSED");
            raf.seek(0);
            Int4ByteUnsignedLE abww = new Int4ByteUnsignedLE();
            System.out.print("Testing Int4ByteUnsignedLE (" + abww.getPrimitiveDescription() + ") ... ");
            abww.setValueFromString("25755");
            abww.writeValueToFile(raf);
            raf.seek(0);
            abww.setValueFromFile(raf);
            if (abww.getValue().intValue() != 25755) {
                throw new IOException("Int4ByteUnsignedLE read/write not successful");
            }
            System.out.println("PASSED");
            raf.seek(0);
            IntByteSigned aa = new IntByteSigned();
            System.out.print("Testing IntByteSigned (" + aa.getPrimitiveDescription() + ") ... ");
            aa.setValueFromString("-100");
            aa.writeValueToFile(raf);
            raf.seek(0);
            aa.setValueFromFile(raf);
            if (aa.getValue().intValue() != -100) {
                throw new IOException("IntByteSigned read/write not successful");
            }
            System.out.println("PASSED");
            raf.seek(0);
            IntByteUnsigned aas = new IntByteUnsigned();
            System.out.print("Testing IntByteUnsigned (" + aas.getPrimitiveDescription() + ") ... ");
            aas.setValueFromString("223");
            aas.writeValueToFile(raf);
            raf.seek(0);
            aas.setValueFromFile(raf);
            if (aas.getValue().intValue() != 223) {
                throw new IOException("IntByteUnsigned read/write not successful");
            }
            System.out.println("PASSED");
            Int2ByteUnsignedLE val = new Int2ByteUnsignedLE();
            val.setValue(1);
            PrimitiveData pd = new PrimitiveData(val, KlangConstants.PRIMITIVE_DATA_FMT_CHUNK_COMP_CODE, null, null);
            System.out.println("Testing Primitive Data Objects");
            if (!pd.getCurrentValueString().equals("Microsoft PCM/Uncompressed")) {
                throw new IOException("PrimitiveData creation");
            }
            System.out.println("PASSED");
            raf.close();
            deleteTempFile(tempFile);
            System.out.println("ALL TESTS PASSED!");
        } catch (Exception e) {
            System.out.println("ERROR!");
            e.printStackTrace();
            if (tempFile != null) {
                try {
                    deleteTempFile(tempFile);
                } catch (Exception foo) {
                }
            }
        }
    }
