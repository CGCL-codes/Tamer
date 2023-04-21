    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        File zipfile = File.createTempFile("db-", ".zip");
        zipfile.deleteOnExit();
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipfile));
        out.putNextEntry(new ZipEntry("test/"));
        out.closeEntry();
        File file = File.createTempFile("001_up_", ".sql");
        FileWriter writer = new FileWriter(file);
        String content = "This file has no tokens in it";
        writer.append(content);
        writer.close();
        file.deleteOnExit();
        out.putNextEntry(new ZipEntry("test/" + file.getName()));
        out.write(content.getBytes());
        out.closeEntry();
        file = File.createTempFile("002_up_", ".sql");
        writer = new FileWriter(file);
        content = "This file has #SEVERAL() tokens such as\n #SCHEMA() and #TABLESPACE()\n";
        content += "But also has #SEVERAL() in here #SEVERAL() times on #SEVERAL() lines too.";
        content += "But do not forget about #NUM3R1C4L(6) tokens and #UNDER_SCORE() tokens too!";
        writer.append(content);
        writer.close();
        file.deleteOnExit();
        out.putNextEntry(new ZipEntry("test/" + file.getName()));
        out.write(content.getBytes());
        out.closeEntry();
        for (int i = 3; i < 10; i++) {
            file = File.createTempFile("00" + i + "_up_", ".sql");
            file.deleteOnExit();
            out.putNextEntry(new ZipEntry("test/" + file.getName()));
            out.closeEntry();
            file = File.createTempFile("00" + i + "_down_", ".sql");
            file.deleteOnExit();
            out.putNextEntry(new ZipEntry("test/" + file.getName()));
            out.closeEntry();
        }
        out.close();
        tempDirectory = file.getParentFile();
        tempArchive = new ZipFile(zipfile);
    }
