    boolean[] ReadPBM(String filename) throws IOException {
        BufferedReader fr = new BufferedReader(new FileReader(filename));
        System.out.println("ReadPBM : " + filename);
        String magicNumber = fr.readLine();
        if (!magicNumber.equalsIgnoreCase("P4")) {
            throw new IOException("Something wrong with PBM file : magic number");
        }
        String dimString = fr.readLine();
        StringTokenizer st = new StringTokenizer(dimString);
        if (st.countTokens() != 2) {
            throw new IOException("Something wrong with PBM file : Second line doesnt have 2 tokens");
        }
        width = Integer.parseInt(st.nextToken());
        height = Integer.parseInt(st.nextToken());
        System.out.println("PBM image " + width + " x " + height);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int byteCount = 0;
        while (fr.ready()) {
            out.write(fr.read());
            byteCount++;
        }
        if (byteCount != width * height / 8) {
            throw new IOException("Read incorrect number of bytes!");
        }
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        BitReader br = new BitReader(in);
        boolean[] bits = new boolean[byteCount * 8];
        for (int i = 0; i < bits.length; i++) {
            if (i % width == 0) {
                if (width < 50) System.out.println("");
            }
            int bit = br.readBit();
            if (bit < 0 || bit > 1) throw new IOException("Failure reading from in memory buffer"); else if (bit == 1) {
                if (width < 50) System.out.print(1);
                bits[i] = true;
            } else {
                if (width < 50) System.out.print(0);
                bits[i] = false;
            }
        }
        if (width < 50) System.out.println("");
        return bits;
    }
