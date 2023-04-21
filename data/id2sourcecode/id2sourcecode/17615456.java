    @Test
    public final void test2() {
        final Whirlpool md = new Whirlpool();
        md.update("-1".getBytes());
        md.digest();
    }
