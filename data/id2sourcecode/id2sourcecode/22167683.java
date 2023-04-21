    public void writeTo(OutputStream os, String as[]) throws IOException, MessagingException {
        if (isparsed) {
            super.writeTo(os, as);
            return;
        }
        if (as != null) {
            parse();
            super.writeTo(os, as);
            return;
        }
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(getFile());
            byte buff[] = new byte[8192];
            int i = 0;
            while ((i = fis.read(buff)) > 0) os.write(buff, 0, i);
        } catch (Exception ex) {
            throw new MessagingException("unable to retrieve message stream", ex);
        } finally {
            if (fis != null) try {
                fis.close();
            } catch (Exception ex) {
            }
            if (os != null) try {
                os.flush();
            } catch (Exception ex) {
            }
        }
    }
