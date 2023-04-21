    static void replaceStyleSheet(File compareDir) throws IOException {
        File patchFile = new File(compareDir, "items.xsl.patch");
        if (patchFile.exists()) {
            logger.info("Found " + patchFile + "; assuming items.xsl has been patched");
            return;
        }
        File xsl = new File(compareDir, "items.xsl");
        logger.info("Replacing " + xsl + " with modified version");
        if (!xsl.exists()) throw new IllegalArgumentException("Compare directory appears incorrect: expected it to contain 'items.xsl'");
        if (!patchProg.exists()) throw new IllegalArgumentException("GNU patch program '" + patchProg + "' not found");
        InputStream is = MergePeopleCodeTrees.class.getResourceAsStream("/items.xsl.patch");
        OutputStream os = new FileOutputStream(patchFile);
        int i;
        while ((i = is.read()) >= 0) os.write(i);
        is.close();
        os.close();
        String[] cmdArray = { patchProg.toString(), "", xsl.toString(), patchFile.toString() };
        Process p = Runtime.getRuntime().exec(cmdArray);
        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream())), brErr = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        String line = "", line2 = null;
        while ((line = br.readLine()) != null || ((line2 = brErr.readLine()) != null)) {
            logger.info(line);
            if (line2 != null) logger.severe("patch:  " + line2);
        }
        int exit = p.exitValue();
        if (exit != 0) logger.severe("Unable to patch stylesheet " + xsl + "; exit code = " + exit);
    }
