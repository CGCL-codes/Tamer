    public void generateSMIME(InputStream document, Signature signature, OutputStream smime) throws IOException {
        SMIMEGenerator smimeGenerator;
        try {
            Class c = factory.getMasterClassLoader().loadClass("es.caib.signatura.provider.impl.common.SMIMEGeneratorImpl");
            smimeGenerator = (SMIMEGenerator) c.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        InputStream in = smimeGenerator.generateSMIME(document, signature);
        byte b[] = new byte[8192];
        int read;
        while ((read = in.read(b)) > 0) {
            smime.write(b, 0, read);
        }
        in.close();
    }
