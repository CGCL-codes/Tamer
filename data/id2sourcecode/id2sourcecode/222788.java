    public static void main(String argv[]) throws Exception {
        exprcomp compiler = new exprcomp(new StreamTokenizer(new FileInputStream(argv[0])));
        compiler.parse();
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        compiler.write(new DataOutputStream(data));
        dynaloader dl = new dynaloader(data.toByteArray());
        dl.exec();
    }
