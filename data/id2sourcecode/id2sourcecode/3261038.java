    public static boolean verify(Puzzle puzzle) {
        byte[] keyMaterial = new byte[Puzzle.KEY_MAT_LENGTH];
        byte[] randBytes = new byte[Puzzle.SOLUTION_LENGTH];
        byte[] hash = new byte[Puzzle.SOLUTION_LENGTH];
        int offset = 0;
        int shift = Puzzle.MASK_LENGTH * 8 - puzzle.getComplexity();
        long startTime = System.currentTimeMillis();
        long currentTime = System.currentTimeMillis();
        SHA1Digest md = new SHA1Digest();
        SecureRandom rand = new SecureRandom();
        BigInteger shiftMask = Puzzle.ONES_MASK.shiftRight(shift);
        System.arraycopy(puzzle.getRandom(), 0, keyMaterial, offset, puzzle.getRandom().length);
        offset = puzzle.getRandom().length;
        System.arraycopy(puzzle.getHitI(), 0, keyMaterial, offset, puzzle.getHitI().length);
        offset = puzzle.getHitI().length;
        System.arraycopy(puzzle.getHitR(), 0, keyMaterial, offset, puzzle.getHitR().length);
        offset = puzzle.getHitR().length;
        System.arraycopy(puzzle.getSolution(), 0, keyMaterial, offset, puzzle.getSolution().length);
        System.arraycopy(md.digest(keyMaterial), md.HASH_SIZE - Puzzle.SOLUTION_LENGTH, hash, 0, Puzzle.SOLUTION_LENGTH);
        BigInteger solutionMASK = new BigInteger(hash);
        if (Arrays.equals(solutionMASK.and(shiftMask).toByteArray(), Puzzle.ZERO_MASK.toByteArray())) return true;
        return false;
    }
