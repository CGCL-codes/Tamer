    public void engineNextBytes(byte[] bytes) {
        ensureIsSeeded();
        int loc = 0;
        while (loc < bytes.length) {
            int copy = Math.min(bytes.length - loc, SEED_SIZE - datapos);
            if (copy > 0) {
                System.arraycopy(data, datapos, bytes, loc, copy);
                datapos += copy;
                loc += copy;
            } else {
                System.arraycopy(seed, 0, data, SEED_SIZE, SEED_SIZE);
                byte[] digestdata = digest.digest(data);
                System.arraycopy(digestdata, 0, data, 0, SEED_SIZE);
                datapos = 0;
            }
        }
    }
