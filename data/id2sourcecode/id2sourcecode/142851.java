    public static void writePackages(String filename) {
        HDF data = makePackageHDF();
        int i = 0;
        for (PackageInfo pkg : choosePackages()) {
            writePackage(pkg);
            data.setValue("docs.packages." + i + ".name", pkg.name());
            data.setValue("docs.packages." + i + ".link", pkg.htmlPage());
            TagInfo.makeHDF(data, "docs.packages." + i + ".shortDescr", pkg.firstSentenceTags());
            i++;
        }
        setPageTitle(data, "Package Index");
        TagInfo.makeHDF(data, "root.descr", Converter.convertTags(root.inlineTags(), null));
        ClearPage.write(data, "packages.cs", filename);
        ClearPage.write(data, "package-list.cs", javadocDir + "package-list");
        Proofread.writePackages(filename, Converter.convertTags(root.inlineTags(), null));
    }
