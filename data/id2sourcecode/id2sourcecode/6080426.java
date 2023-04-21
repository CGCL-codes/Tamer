    public static ClassFile parse(InputStream is) throws ParseException, IOException {
        InputStream input = null;
        if (config.getBoolean("alt.jiapi.file.use-ZipFileInputStream-bug-workaround", true)) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream(is.available());
            int i = 0;
            while ((i = is.read()) != -1) {
                bos.write(i);
            }
            input = new ByteArrayInputStream(bos.toByteArray());
        } else {
            input = is;
        }
        DataInputStream dis = new DataInputStream(input);
        ClassFile cf = null;
        try {
            cf = new ClassFile();
            cf.parseClassFile(dis);
            if (dis.available() != 0) {
                System.out.println(is.available() + ":::" + dis.available() + ":" + is);
                System.out.println("" + dis.readByte());
            }
        } catch (EOFException eof) {
            System.out.println(">> Got EOFException: " + eof + "," + is.available() + ", " + cf.getClassName());
        } catch (IOException ioe) {
            System.out.println("Got IOException: " + ioe + "," + is.available() + ", " + cf.getClassName());
            throw new ParseException(ioe.getMessage(), cf);
        } finally {
            dis.close();
        }
        return cf;
    }
