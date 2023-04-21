    private static synchronized String turnintoString(GSSCredential cred) throws IOException, GSSException {
        log.debug("turning credential into string");
        ExtendedGSSCredential extendcred = (ExtendedGSSCredential) cred;
        byte[] data = extendcred.export(ExtendedGSSCredential.IMPEXP_OPAQUE);
        File file = new File(System.getProperty("java.io.tmpdir") + File.separator + "globuscred.txt");
        FileOutputStream out = new FileOutputStream(file);
        out.write(data);
        out.close();
        URL url1 = new URL("file:///" + file.getAbsolutePath());
        URLConnection con = url1.openConnection();
        InputStream in2 = con.getInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(in2));
        String inputLine;
        StringBuffer cert = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            cert.append(inputLine);
            cert.append("\n");
        }
        in.close();
        in2.close();
        out.close();
        log.trace("Deleted file: " + file.getAbsolutePath() + " ? " + file.delete());
        return cert.toString();
    }
