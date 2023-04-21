    private void addReadmeCommentFile(ZipOutputStream zipOutputStream) {
        ZipEntry entry = new ZipEntry("_READ_ME.JRAT");
        try {
            zipOutputStream.putNextEntry(entry);
            PrintStream printStream = new PrintStream(zipOutputStream);
            printStream.println("# This Archive file was injected by");
            printStream.println("# Shiftone JRat the Java Runtime Analysis Toolkit");
            printStream.println("# For more information, visit http://jrat.sourceforge.net");
            printStream.println("#\tVersion  : " + VersionUtil.getVersion());
            printStream.println("#\tBuilt On : " + VersionUtil.getBuiltOn());
            printStream.println("#\tBuilt By : " + VersionUtil.getBuiltBy());
            printStream.println();
            printStream.println();
            printStream.println();
            printStream.println("# the following system properties were present during injection");
            printStream.flush();
            System.getProperties().store(zipOutputStream, null);
            zipOutputStream.closeEntry();
        } catch (IOException e) {
            throw new JRatException("unable to add comment file to archive", e);
        }
    }
