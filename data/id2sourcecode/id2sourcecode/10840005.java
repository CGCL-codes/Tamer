    public void addLangPack(String iso3, InputStream input) throws Exception {
        sendMsg("Adding langpack : " + iso3 + " ...");
        langpacks.add(iso3);
        outJar.putNextEntry(new ZipEntry("langpacks/" + iso3 + ".xml"));
        copyStream(input, outJar);
        outJar.closeEntry();
        input.close();
    }
