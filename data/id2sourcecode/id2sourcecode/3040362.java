    static void insertLicense(File dir) throws Exception {
        BufferedReader in = null;
        PrintWriter out = null;
        String line = null;
        for (File f : dir.listFiles(new FileFilter() {

            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".java");
            }
        })) {
            if (f.isDirectory()) {
                insertLicense(f);
                continue;
            }
            File to = new File(f.getAbsolutePath() + ".tmp");
            if (alreadyLicensed(f)) {
                System.out.println("Skipping licensed file " + f.getPath());
                continue;
            } else {
                System.out.println("Adding license to " + f.getPath());
            }
            in = new BufferedReader(new FileReader(f));
            out = new PrintWriter(new FileWriter(to));
            out.println(commentData[JAVA][PRE]);
            out.print(commentData[JAVA][LINE]);
            out.println(LICENSE_START);
            for (String s : licenseLines) {
                out.print(commentData[JAVA][LINE]);
                out.println(s);
            }
            out.print(commentData[JAVA][LINE]);
            out.println(LICENSE_END);
            out.println(commentData[JAVA][POST]);
            while (null != (line = in.readLine())) {
                out.println(line);
            }
            out.close();
            in.close();
            f.delete();
            to.renameTo(f);
        }
    }
