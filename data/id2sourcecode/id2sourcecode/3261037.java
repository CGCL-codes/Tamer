    public static boolean solve(Puzzle puzzle, long ms) {
        byte[] keyMaterial = new byte[Puzzle.KEY_MAT_LENGTH];
        byte[] randBytes = new byte[Puzzle.SOLUTION_LENGTH];
        byte[] solution = new byte[Puzzle.SOLUTION_LENGTH];
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
        offset += puzzle.getHitI().length;
        System.arraycopy(puzzle.getHitR(), 0, keyMaterial, offset, puzzle.getHitR().length);
        offset += puzzle.getHitR().length;
        while (true) {
            currentTime = System.currentTimeMillis();
            if (currentTime - startTime > ms) break;
            rand.nextBytes(randBytes);
            System.arraycopy(randBytes, 0, keyMaterial, offset, randBytes.length);
            System.arraycopy(md.digest(keyMaterial), md.HASH_SIZE - Puzzle.SOLUTION_LENGTH, solution, 0, Puzzle.SOLUTION_LENGTH);
            BigInteger solutionMASK = new BigInteger(solution);
            if (Arrays.equals(solutionMASK.and(shiftMask).toByteArray(), Puzzle.ZERO_MASK.toByteArray())) {
                puzzle.setSolution(randBytes);
                return true;
            }
        }
        return false;
    }
