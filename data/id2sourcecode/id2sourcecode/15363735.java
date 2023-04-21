    @Test
    public void writeToJarThenRereadFromJarAndEnsureContentIsEqual() throws IOException {
        ArchiveScriptLocation originalScriptArchive = new ArchiveScriptLocation(scripts, "ISO-8859-1", "postprocessing", Collections.singleton("PATCH"), "#", "@", CollectionUtils.asSet("sql", "ddl"));
        originalScriptArchive.writeToJarFile(jarFile);
        ArchiveScriptLocation scriptArchiveFromFile = new ArchiveScriptLocation(jarFile, "ISO-8859-1", "postprocessing", Collections.singleton("PATCH"), "#", "@", CollectionUtils.asSet("sql", "ddl"));
        assertEqualProperties(originalScriptArchive, scriptArchiveFromFile);
        assertEqualScripts(originalScriptArchive.getScripts(), scriptArchiveFromFile.getScripts());
    }
