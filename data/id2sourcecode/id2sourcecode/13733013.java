    public static int checkRevision(String versionString, String[] files, int mpqNum) throws FileNotFoundException, IOException {
        Integer cacheHit = (Integer) crCache.get(versionString + mpqNum + files[0]);
        if (cacheHit != null) {
            crCacheHits++;
            Out.println("CREV", "CheckRevision cache hit: " + crCacheHits + " hits, " + crCacheMisses + " misses.");
            return cacheHit.intValue();
        }
        crCacheMisses++;
        Out.println("CREV", "CheckRevision cache miss: " + crCacheHits + " hits, " + crCacheMisses + " misses.");
        StringTokenizer tok = new StringTokenizer(versionString, " ");
        int a = Integer.parseInt(tok.nextToken().substring(2));
        int b = Integer.parseInt(tok.nextToken().substring(2));
        int c = Integer.parseInt(tok.nextToken().substring(2));
        tok.nextToken();
        String formula;
        formula = tok.nextToken();
        if (formula.matches("A=A.S") == false) return checkRevisionSlow(versionString, files, mpqNum);
        char op1 = formula.charAt(3);
        formula = tok.nextToken();
        if (formula.matches("B=B.C") == false) return checkRevisionSlow(versionString, files, mpqNum);
        char op2 = formula.charAt(3);
        formula = tok.nextToken();
        if (formula.matches("C=C.A") == false) return checkRevisionSlow(versionString, files, mpqNum);
        char op3 = formula.charAt(3);
        formula = tok.nextToken();
        if (formula.matches("A=A.B") == false) return checkRevisionSlow(versionString, files, mpqNum);
        char op4 = formula.charAt(3);
        a ^= hashcodes[mpqNum];
        for (int i = 0; i < files.length; i++) {
            File currentFile = new File(files[i]);
            int roundedSize = (int) ((currentFile.length() / 1024) * 1024);
            MappedByteBuffer fileData = new FileInputStream(currentFile).getChannel().map(FileChannel.MapMode.READ_ONLY, 0, roundedSize);
            fileData.order(ByteOrder.LITTLE_ENDIAN);
            for (int j = 0; j < roundedSize; j += 4) {
                int s = fileData.getInt(j);
                switch(op1) {
                    case '^':
                        a = a ^ s;
                        break;
                    case '-':
                        a = a - s;
                        break;
                    case '+':
                        a = a + s;
                        break;
                }
                switch(op2) {
                    case '^':
                        b = b ^ c;
                        break;
                    case '-':
                        b = b - c;
                        break;
                    case '+':
                        b = b + c;
                        break;
                }
                switch(op3) {
                    case '^':
                        c = c ^ a;
                        break;
                    case '-':
                        c = c - a;
                        break;
                    case '+':
                        c = c + a;
                        break;
                }
                switch(op4) {
                    case '^':
                        a = a ^ b;
                        break;
                    case '-':
                        a = a - b;
                        break;
                    case '+':
                        a = a + b;
                        break;
                }
            }
        }
        System.gc();
        crCache.put(versionString + mpqNum + files[0], new Integer(c));
        return c;
    }
