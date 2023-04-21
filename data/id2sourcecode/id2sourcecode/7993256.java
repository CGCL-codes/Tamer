    private synchronized String getSerialNumber() {
        java.net.URL file = this.getClass().getResource(serial_file);
        if (file == null) {
            if (verbose) System.out.println("Config file not found " + serial_file);
        }
        try {
            BufferedReader in = new BufferedReader(new java.io.FileReader(file.getFile()));
            String serial = in.readLine();
            if (serial == null) serial = "" + System.currentTimeMillis();
            String s;
            MessageDigest md = MessageDigest.getInstance("SHA");
            BigInteger bi = new BigInteger(serial, 16);
            md.update(bi.toByteArray());
            byte[] result = md.digest();
            BigInteger bir = new BigInteger(result);
            bir = bir.abs();
            s = bir.toString(16);
            File outfile = new File(file.getFile());
            FileOutputStream f = new FileOutputStream(outfile);
            f.write(s.getBytes());
            f.close();
            return s;
        } catch (FileNotFoundException fnfe) {
            if (verbose) System.out.println("Config file not found " + serial_file);
        } catch (IOException ioe) {
            if (verbose) ioe.printStackTrace();
        } catch (NoSuchAlgorithmException nsae) {
            if (verbose) nsae.printStackTrace();
        }
        return null;
    }
