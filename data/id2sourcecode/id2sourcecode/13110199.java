    public static void demoFile(final File aFile) {
        if (!aFile.isFile()) return;
        System.out.println();
        System.out.println("-- file " + aFile.getPath());
        System.out.flush();
        final StringBuilder document = new StringBuilder();
        try {
            final BufferedReader instream = new BufferedReader(new FileReader(aFile));
            final char[] buffer = new char[1024];
            int nRead = -1;
            while (-1 != (nRead = instream.read(buffer))) document.append(buffer, 0, nRead);
            write.print(read.parse(document));
        } catch (ChattyParseException e) {
            System.out.println(showErrorLine(document, e));
            System.out.println(e.getProblem() + (e.getLexeme() != null ? " (at " + e.getLexeme().toString() + ")" : ""));
            e.printStackTrace(System.out);
        } catch (ChattyInternalError e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.flush();
        System.err.flush();
    }
