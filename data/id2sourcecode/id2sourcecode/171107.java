    public static void writePackage(PackageInfo pkg) {
        HDF data = makePackageHDF();
        String name = pkg.name();
        data.setValue("package.name", name);
        data.setValue("package.since", pkg.getSince());
        data.setValue("package.descr", "...description...");
        makeClassListHDF(data, "package.interfaces", ClassInfo.sortByName(pkg.interfaces()));
        makeClassListHDF(data, "package.classes", ClassInfo.sortByName(pkg.ordinaryClasses()));
        makeClassListHDF(data, "package.enums", ClassInfo.sortByName(pkg.enums()));
        makeClassListHDF(data, "package.exceptions", ClassInfo.sortByName(pkg.exceptions()));
        makeClassListHDF(data, "package.errors", ClassInfo.sortByName(pkg.errors()));
        TagInfo.makeHDF(data, "package.shortDescr", pkg.firstSentenceTags());
        TagInfo.makeHDF(data, "package.descr", pkg.inlineTags());
        String filename = pkg.htmlPage();
        setPageTitle(data, name);
        ClearPage.write(data, "package.cs", filename);
        filename = pkg.fullDescriptionHtmlPage();
        setPageTitle(data, name + " Details");
        ClearPage.write(data, "package-descr.cs", filename);
        Proofread.writePackage(filename, pkg.inlineTags());
    }
