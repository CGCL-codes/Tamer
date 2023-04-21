    public boolean equals(Wad wad) {
        if (identifier != wad.getWadIdentifier() || getNumberOfLumps() != wad.getNumberOfLumps()) {
            return false;
        }
        MessageDigest messagedigest;
        ArrayList<Integer> checked = new ArrayList<Integer>();
        byte[] thisdigest, comparedigest;
        try {
            messagedigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return false;
        }
        for (int i = 0; i < getNumberOfLumps(); i++) {
            messagedigest.update(getAllLumps().get(i).getRawLumpData().getByteBuffer());
            thisdigest = messagedigest.digest();
            for (int j = 0; j < wad.getNumberOfLumps(); j++) {
                if (!checked.contains(new Integer(j)) && getAllLumps().get(i).getName().equals(wad.getAllLumps().get(j).getName()) && getAllLumps().get(i).getSize() == wad.getAllLumps().get(j).getSize()) {
                    messagedigest.update(wad.getAllLumps().get(j).getRawLumpData().getByteBuffer());
                    comparedigest = messagedigest.digest();
                    if (MessageDigest.isEqual(thisdigest, comparedigest)) {
                        checked.add(j);
                        break;
                    }
                }
                if (j == wad.getNumberOfLumps() - 1) {
                    return false;
                }
            }
        }
        return true;
    }
