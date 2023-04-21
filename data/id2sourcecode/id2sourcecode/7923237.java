    public static void getFile(URL url, File saveFile, boolean backUpExisting, boolean verbose) {
        if (verbose) {
            System.err.println("Downloading " + url + " to " + saveFile);
        }
        File target;
        String baseName;
        if (!saveFile.isDirectory()) {
            target = saveFile;
            baseName = saveFile.getName();
        } else {
            File baseFile = new File(url.getFile());
            baseName = baseFile.getName();
            if (baseName.length() == 0) {
                baseName = "file";
            }
            target = new File(saveFile, baseName);
        }
        URLConnection conn;
        try {
            conn = url.openConnection();
        } catch (IOException ioe) {
            System.err.println("Couldn't open \"" + url + "\"");
            return;
        }
        if (target.exists()) {
            conn.setIfModifiedSince(target.lastModified());
        }
        if (conn.getContentLength() < 0) {
            if (verbose) {
                System.err.println(url + " is not newer than " + target);
            }
            return;
        }
        if (backUpExisting && target.exists()) {
            int idx = 0;
            while (true) {
                File tmpFile = new File(saveFile, baseName + "." + idx);
                if (!tmpFile.exists()) {
                    if (!target.renameTo(tmpFile)) {
                        System.err.println("Couldn't rename \"" + target + "\" to \"" + tmpFile + "\"");
                        target.delete();
                    }
                    break;
                }
                idx++;
            }
        }
        BufferedInputStream in;
        try {
            InputStream uIn = conn.getInputStream();
            in = new BufferedInputStream(uIn);
        } catch (IOException ioe) {
            System.err.println("Couldn't read \"" + url + "\"");
            return;
        }
        BufferedOutputStream out;
        try {
            FileOutputStream fOut = new FileOutputStream(target);
            out = new BufferedOutputStream(fOut);
        } catch (IOException ioe) {
            System.err.println("Couldn't write \"" + target + "\"");
            return;
        }
        byte[] block = new byte[1024];
        while (true) {
            int len;
            try {
                len = in.read(block);
            } catch (IOException ioe) {
                ioe.printStackTrace();
                break;
            }
            if (len < 0) {
                break;
            }
            try {
                out.write(block, 0, len);
            } catch (IOException ioe) {
                ioe.printStackTrace();
                break;
            }
        }
        try {
            out.close();
        } catch (IOException ioe) {
        }
        try {
            in.close();
        } catch (IOException ioe) {
        }
        long connMod = conn.getLastModified();
        if (connMod != 0) {
            target.setLastModified(connMod);
        }
        if (verbose) {
            System.out.println("Successfully updated " + target);
        }
    }
