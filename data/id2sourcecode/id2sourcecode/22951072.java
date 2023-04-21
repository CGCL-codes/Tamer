    public PhonerProfileEnum(String pnumber) throws IllegalArgumentException {
        length = pnumber.length();
        charSets = new byte[length][];
        for (i = 0; i < length; i++) {
            String ch = String.valueOf(pnumber.charAt(i));
            charSets[i] = (byte[]) replacers.get(ch);
            if (charSets[i] == null) charSets[i] = new byte[] { ch.getBytes()[0] };
        }
        bytes = new byte[length + 2];
        bytes[0] = bytes[i + 1] = (byte) ' ';
        j = new int[length];
        i = 0;
        j[0] = -1;
        grams = new IntMap(3 << (2 * length));
        gtcount = new int[length + 1];
        eqcount = new int[length + 1];
    }
