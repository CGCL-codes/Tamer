    private static void write(String name, String comment, String string, ZipOutputStream out) throws IOException {
        ZipEntry entry = new ZipEntry(name);
        entry.setComment(comment);
        out.putNextEntry(entry);
        PrintStream print = new PrintStream(out);
        print.println(string);
        print.flush();
        out.closeEntry();
    }
