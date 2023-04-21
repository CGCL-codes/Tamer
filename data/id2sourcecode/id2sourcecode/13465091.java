    @Test
    public void testMd5() throws Throwable {
        String aaa = DataUtil.digest("aaa".getBytes());
        System.out.println("aaa:" + aaa);
    }
