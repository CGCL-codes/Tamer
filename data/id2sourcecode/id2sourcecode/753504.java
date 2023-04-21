    public static void writeClass(ClassInfo cl, HDF data) {
        cl.makeHDF(data);
        setPageTitle(data, cl.name());
        ClearPage.write(data, "class.cs", cl.htmlPage());
        Proofread.writeClass(cl.htmlPage(), cl);
    }
