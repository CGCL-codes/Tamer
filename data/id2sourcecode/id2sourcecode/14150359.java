    public static byte[] unzip(byte[] input) {
        Misc.log.finest("Unzipping input with length " + Misc.prettyPrintSize(input.length));
        try {
            ByteArrayInputStream bla = new ByteArrayInputStream(input);
            GZIPInputStream gzis = new GZIPInputStream(bla);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            while (gzis.available() > 0) out.write(gzis.read());
            out.flush();
            byte[] output = out.toByteArray();
            gzis.close();
            bla.close();
            out.close();
            Misc.log.finest("Unzipping output length: " + Misc.prettyPrintSize(output.length));
            return copyfromto(output, 0, output.length - 1);
        } catch (Exception e) {
            Misc.log.log(Level.WARNING, "", e);
        }
        return null;
    }
