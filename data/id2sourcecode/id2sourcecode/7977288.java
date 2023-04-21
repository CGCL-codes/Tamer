    public static String read(String file) {
        StringWriter txt = new StringWriter();
        try {
            FileInputStream f = new FileInputStream(file);
            for (int count = f.read(); count != -1; count = f.read()) txt.write((char) count);
            f.close();
        } catch (FileNotFoundException _ex) {
        } catch (SecurityException _ex) {
        } catch (IOException _ex) {
        }
        return txt.toString();
    }
