    private void dumpToPDF(File dirOut, String oname, String ff, Book book, Hashtable<String, String> repl) {
        String ret = null;
        try {
            System.out.print("Creating " + ff + ".pdf...");
            if (zout != null) {
                zout.putNextEntry(new ZipEntry(ff + ".pdf"));
                ret = book.getPDF(zout, lrfSize, repl);
            } else if (dirOut != null) {
                File nf = new File(dirOut, ff + ".pdf");
                nf.getParentFile().mkdirs();
                FileOutputStream fos = new FileOutputStream(nf);
                ret = book.getPDF(fos, lrfSize, repl);
                fos.close();
            } else {
                FileOutputStream fos = new FileOutputStream(oname + ".pdf");
                ret = book.getPDF(fos, lrfSize, repl);
                fos.close();
            }
            System.out.println(ret);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("PDF Error");
        }
    }
