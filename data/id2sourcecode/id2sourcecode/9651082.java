    public String toString() {
        int BUFFER_SIZE = 32 * 1024;
        String digestString = "";
        int i = 0;
        if (tipoProceso) {
            try {
                FileInputStream fichero = new FileInputStream(this.cadenaParaProcesar);
                BufferedInputStream file = new BufferedInputStream(fichero);
                MessageDigest mdfichero = MessageDigest.getInstance(this.algoritmo);
                DigestInputStream in = new DigestInputStream(file, mdfichero);
                byte[] buffer = new byte[BUFFER_SIZE];
                do {
                    i = in.read(buffer, 0, BUFFER_SIZE);
                } while (i == BUFFER_SIZE);
                mdfichero = in.getMessageDigest();
                in.close();
                byte[] digest = mdfichero.digest();
                digestString = hexString(digest);
                file.close();
                fichero.close();
            } catch (SecurityException e) {
                System.out.println("Error de seguridad.");
            } catch (Exception e) {
                System.err.println("Error: " + e);
            }
        } else {
            try {
                MessageDigest md = MessageDigest.getInstance(this.algoritmo);
                byte[] digest = new byte[md.getDigestLength()];
                digest = md.digest(cadenaParaProcesar.getBytes());
                digestString = hexString(digest);
            } catch (SecurityException e) {
                System.out.println("Error de seguridad.");
            } catch (Exception e) {
                System.err.println("Error: " + e);
            }
        }
        return digestString;
    }
