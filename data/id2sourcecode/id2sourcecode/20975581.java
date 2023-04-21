    public byte[] findCollisionForHash() {
        byte[] input = startInput.clone();
        do {
            if (Arrays.equals(hash, digest.digest(input))) {
                return input;
            }
            input = generateNextInput(input);
        } while (!Arrays.equals(input, endInput));
        return null;
    }
