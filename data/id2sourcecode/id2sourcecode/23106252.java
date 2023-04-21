    public static void writeClass(ClassInfo cl, Data data) {
        cl.makeHDF(data);
        setPageTitle(data, cl.name());
        ClearPage.write(data, "class.cs", Doclava.javadocDir + cl.relativePath());
        Proofread.writeClass(cl.htmlPage(), cl);
    }
