    private void dumpToXML(File dirOut, String oname, String ff, Book book, Hashtable<String, String> repl) {
        try {
            System.out.print("Creating " + ff + ".xml...");
            if (zout != null) {
                zout.putNextEntry(new ZipEntry(ff + ".xml"));
                book.getXML(zout, repl);
            } else if (dirOut != null) {
                File nf = new File(dirOut, ff + ".xml");
                nf.getParentFile().mkdirs();
                FileOutputStream fos = new FileOutputStream(nf);
                book.getXML(fos, repl);
                fos.close();
            } else {
                FileOutputStream fos = new FileOutputStream(oname + ".xml");
                book.getXML(fos, repl);
                fos.close();
            }
            System.out.println("XML Ok");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("XML Error");
        }
    }
