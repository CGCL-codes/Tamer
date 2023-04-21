    protected void copy() {
        String link;
        String raw;
        String name;
        File file;
        File dir;
        URL source;
        byte[] data;
        InputStream in;
        FileOutputStream out;
        int read;
        link = (String) mImages.remove(0);
        mCopied.add(link);
        if (getCaptureResources()) {
            raw = makeLocalLink(link, "");
            name = decode(raw);
            file = new File(getTarget(), name);
            System.out.println("copying " + link + " to " + file.getAbsolutePath());
            dir = file.getParentFile();
            if (!dir.exists()) dir.mkdirs();
            try {
                source = new URL(link);
                data = new byte[TRANSFER_SIZE];
                try {
                    in = source.openStream();
                    try {
                        out = new FileOutputStream(file);
                        try {
                            while (-1 != (read = in.read(data, 0, data.length))) out.write(data, 0, read);
                        } finally {
                            out.close();
                        }
                    } catch (FileNotFoundException fnfe) {
                        fnfe.printStackTrace();
                    } finally {
                        in.close();
                    }
                } catch (FileNotFoundException fnfe) {
                    System.err.println("broken link " + fnfe.getMessage() + " ignored");
                }
            } catch (MalformedURLException murle) {
                murle.printStackTrace();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
