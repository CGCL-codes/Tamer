    public void generateSMIMEParalell(InputStream document, Signature[] signatures, OutputStream smime) throws IOException, SignatureException {
        try {
            Class c = factory.getMasterClassLoader().loadClass("es.caib.signatura.provider.impl.common.GeneradorSMIMEParaleloImpl");
            GeneradorSMIMEParalelo generadorSMIMEParalelo = (GeneradorSMIMEParalelo) c.newInstance();
            SMIMEInputStream in = generadorSMIMEParalelo.generarSMIMEParalelo(document, signatures);
            byte b[] = new byte[8192];
            int read;
            while ((read = in.read(b)) > 0) {
                smime.write(b, 0, read);
            }
            in.close();
        } catch (IOException iox) {
            throw iox;
        } catch (Exception e) {
            throw new SignatureException(e.getMessage());
        }
    }
