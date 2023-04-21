    public static int[] hash(char[] data) {
        WardenSHA1 ctx = new WardenSHA1();
        ctx.update(data);
        return ctx.digest();
    }
