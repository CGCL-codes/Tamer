    public static void writePackage(PackageInfo pkg) {
        Data data = makePackageHDF();
        String name = pkg.name();
        data.setValue("package.name", name);
        data.setValue("package.since.key", SinceTagger.keyForName(pkg.getSince()));
        data.setValue("package.since.name", pkg.getSince());
        data.setValue("package.descr", "...description...");
        pkg.setFederatedReferences(data, "package");
        makeClassListHDF(data, "package.annotations", ClassInfo.sortByName(pkg.getAnnotations()));
        makeClassListHDF(data, "package.interfaces", ClassInfo.sortByName(pkg.getInterfaces()));
        makeClassListHDF(data, "package.classes", ClassInfo.sortByName(pkg.ordinaryClasses()));
        makeClassListHDF(data, "package.enums", ClassInfo.sortByName(pkg.enums()));
        makeClassListHDF(data, "package.exceptions", ClassInfo.sortByName(pkg.exceptions()));
        makeClassListHDF(data, "package.errors", ClassInfo.sortByName(pkg.errors()));
        TagInfo[] shortDescrTags = pkg.firstSentenceTags();
        TagInfo[] longDescrTags = pkg.inlineTags();
        TagInfo.makeHDF(data, "package.shortDescr", shortDescrTags);
        TagInfo.makeHDF(data, "package.descr", longDescrTags);
        data.setValue("package.hasLongDescr", TagInfo.tagsEqual(shortDescrTags, longDescrTags) ? "0" : "1");
        String filename = Doclava.javadocDir + pkg.relativePath();
        setPageTitle(data, name);
        ClearPage.write(data, "package.cs", filename);
        filename = javadocDir + pkg.fullDescriptionFile();
        setPageTitle(data, name + " Details");
        ClearPage.write(data, "package-descr.cs", filename);
        Proofread.writePackage(filename, pkg.inlineTags());
    }
