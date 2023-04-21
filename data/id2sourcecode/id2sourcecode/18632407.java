    private synchronized String getSerialNumber() {
        log.debug("Start getting the serialnumber");
        try {
            byte[] serial;
            java.net.URL file = null;
            file = this.getClass().getResource(DISConfig.serial_file);
            if (file == null) {
                String tmp = "" + System.currentTimeMillis();
                serial = tmp.getBytes();
            } else {
                java.io.InputStream is = new FileInputStream(file.getFile());
                serial = new byte[is.available()];
                is.read(serial);
                if (serial.length == 0) {
                    String tmp = "" + System.currentTimeMillis();
                    serial = tmp.getBytes();
                }
            }
            String s;
            MessageDigest md = MessageDigest.getInstance("SHA");
            BigInteger bi = new BigInteger(serial);
            md.update(bi.toByteArray());
            byte[] result = md.digest();
            BigInteger bir = new BigInteger(result);
            bir = bir.abs();
            s = bir.toString(16);
            log.debug("Serial number is : " + s);
            File outfile = dcfg.getSerialFile();
            FileOutputStream f = new FileOutputStream(outfile);
            f.write(s.getBytes());
            f.close();
            return s;
        } catch (IOException ioe) {
            log.error("File not found: " + ioe.getMessage() + "\n" + ioe.fillInStackTrace());
        } catch (NoSuchAlgorithmException nsae) {
            log.error("Algorithm is not found: " + nsae.getMessage() + "\n" + nsae.fillInStackTrace());
        }
        return null;
    }
