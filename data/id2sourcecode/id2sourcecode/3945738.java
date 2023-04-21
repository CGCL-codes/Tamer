    private void dumpToHTML(File dirOut, String oname, String ff, Book book, Hashtable<String, String> repl) {
        try {
            System.out.print("Creating " + ff + ".html...");
            File images = new File(dirOut, ff);
            if (zout != null) {
                zout.putNextEntry(new ZipEntry(ff + ".html"));
                book.getHTML(zout, images, images.getName() + "/", repl);
            } else if (dirOut != null) {
                File nf = new File(dirOut, ff + ".html");
                nf.getParentFile().mkdirs();
                FileOutputStream fos = new FileOutputStream(nf);
                book.getHTML(fos, images, images.getName() + "/", repl);
                fos.close();
            } else {
                FileOutputStream fos = new FileOutputStream(oname + ".html");
                book.getHTML(fos, images, images.getName() + "/", repl);
                fos.close();
            }
            System.out.println("HTML Ok");
        } catch (Exception e) {
            System.out.println("HTML Error:");
            e.printStackTrace();
        }
    }
