    private static void writeCert(X509Certificate cert, File file) throws IOException, CertificateEncodingException {
        DefensiveTools.checkNull(cert, "cert");
        DefensiveTools.checkNull(file, "file");
        FileOutputStream fout = new FileOutputStream(file);
        try {
            fout.getChannel().lock();
            fout.write(cert.getEncoded());
        } finally {
            fout.close();
        }
    }
