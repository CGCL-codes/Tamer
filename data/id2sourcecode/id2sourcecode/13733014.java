    private static int checkRevisionSlow(String versionString, String[] files, int mpqNum) throws FileNotFoundException, IOException {
        System.out.println("Warning: using checkRevisionSlow for version string: " + versionString);
        Integer cacheHit = (Integer) crCache.get(versionString + mpqNum + files[0]);
        if (cacheHit != null) {
            crCacheHits++;
            System.out.println("CheckRevision cache hit");
            System.out.println(" --> " + crCacheHits + " hits, " + crCacheMisses + " misses.");
            return cacheHit.intValue();
        }
        crCacheMisses++;
        System.out.println("CheckRevision cache miss");
        System.out.println("--> " + crCacheHits + " hits, " + crCacheMisses + " misses.");
        int[] values = new int[4];
        int[] opValueDest = new int[4];
        int[] opValueSrc1 = new int[4];
        char[] operation = new char[4];
        int[] opValueSrc2 = new int[4];
        StringTokenizer s = new StringTokenizer(versionString, " ");
        int currentFormula = 0;
        while (s.hasMoreTokens()) {
            String thisToken = s.nextToken();
            if (thisToken.indexOf('=') > 0) {
                StringTokenizer nameValue = new StringTokenizer(thisToken, "=");
                if (nameValue.countTokens() != 2) return 0;
                int variable = getNum(nameValue.nextToken().charAt(0));
                String value = nameValue.nextToken();
                if (Character.isDigit(value.charAt(0))) {
                    values[variable] = Integer.parseInt(value);
                } else {
                    opValueDest[currentFormula] = variable;
                    opValueSrc1[currentFormula] = getNum(value.charAt(0));
                    operation[currentFormula] = value.charAt(1);
                    opValueSrc2[currentFormula] = getNum(value.charAt(2));
                    currentFormula++;
                }
            }
        }
        values[0] ^= hashcodes[mpqNum];
        for (int i = 0; i < files.length; i++) {
            File currentFile = new File(files[i]);
            int roundedSize = (int) ((currentFile.length() / 1024) * 1024);
            MappedByteBuffer fileData = new FileInputStream(currentFile).getChannel().map(FileChannel.MapMode.READ_ONLY, 0, roundedSize);
            fileData.order(ByteOrder.LITTLE_ENDIAN);
            for (int j = 0; j < roundedSize; j += 4) {
                values[3] = fileData.getInt(j);
                for (int k = 0; k < currentFormula; k++) {
                    switch(operation[k]) {
                        case '+':
                            values[opValueDest[k]] = values[opValueSrc1[k]] + values[opValueSrc2[k]];
                            break;
                        case '-':
                            values[opValueDest[k]] = values[opValueSrc1[k]] - values[opValueSrc2[k]];
                            break;
                        case '^':
                            values[opValueDest[k]] = values[opValueSrc1[k]] ^ values[opValueSrc2[k]];
                    }
                }
            }
        }
        crCache.put(versionString + mpqNum + files[0], new Integer(values[2]));
        return values[2];
    }
