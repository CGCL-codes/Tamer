    private void dumpToRTF(File dirOut, String oname, String ff, Book book, Hashtable<String, String> repl) {
        try {
            System.out.print("Creating " + ff + ".rtf...");
            if (zout != null) {
                zout.putNextEntry(new ZipEntry(ff + ".rtf"));
                book.getRTF(zout, repl);
            } else if (dirOut != null) {
                File nf = new File(dirOut, ff + ".rtf");
                nf.getParentFile().mkdirs();
                FileOutputStream fos = new FileOutputStream(nf);
                book.getRTF(fos, repl);
                fos.close();
            } else {
                FileOutputStream fos = new FileOutputStream(oname + ".rtf");
                book.getRTF(fos, repl);
                fos.close();
            }
            System.out.println("RTF Ok");
        } catch (Exception e) {
            System.out.println("RTF Error:");
            e.printStackTrace();
        }
    }
