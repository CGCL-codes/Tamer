    @Test
    public void openResourceStream() throws Exception {
        logger.entering("ResManTest", "openResourceStream");
        ResMan resMan = new ResMan("res/global", "res/explorer");
        InputStream is = resMan.openResource("test.txt");
        BufferedInputStream bis = new BufferedInputStream(is);
        int b;
        while ((b = bis.read()) != -1) System.out.write(b);
        System.out.println();
        is = resMan.openResource("error.txt");
        logger.exiting("ResManTest", "openResourceStream");
        logger.info("EMPTY");
    }
