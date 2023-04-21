    public static void main(String[] args) {
        byte[] src = "s 1".getBytes();
        FNV fnv = new FNV();
        fnv.update(src);
        long h = fnv.digest();
        long H = 3647994781L;
        System.out.printf("expect %d got %d\n", H, h);
    }
