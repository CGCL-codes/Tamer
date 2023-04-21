    private void installFile(String name) throws IOException {
        String s = System.getProperty("file.separator");
        String destName = name.substring(name.indexOf("/") + 1);
        File f = new File(this.targetDir + s + destName);
        System.out.println("Installing '" + f.getAbsolutePath() + "'");
        boolean exists = f.isFile();
        InputStream in = getClass().getResourceAsStream(name);
        if (in != null) {
            BufferedInputStream bufIn = new BufferedInputStream(in);
            try {
                OutputStream fout = new BufferedOutputStream(new FileOutputStream(f));
                byte[] bytes = new byte[1024 * 10];
                for (int n = 0; n != -1; n = bufIn.read(bytes)) fout.write(bytes, 0, n);
                fout.close();
            } catch (IOException ioe) {
                if (!exists) throw ioe;
            }
        } else throw new IOException("Found no resource named: " + name);
    }
