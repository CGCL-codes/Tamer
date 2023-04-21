    public static void copyFile(String in, String out) {
        try {
            ClassLoader loader = ClassLoader.getSystemClassLoader();
            InputStream raw = loader.getResourceAsStream(in);
            BufferedInputStream bin = new BufferedInputStream(raw);
            BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(out));
            int i;
            while (-1 != (i = bin.read())) bout.write(i);
            bin.close();
            bout.close();
        } catch (Exception e) {
            System.out.println("in = " + in + '.');
            System.out.println("out = " + out + '.');
            System.err.println(e.getMessage());
            System.out.println("in = " + in + '.');
            System.out.println("out = " + out + '.');
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Can't copy file (" + out + "), see console output!");
        }
    }
