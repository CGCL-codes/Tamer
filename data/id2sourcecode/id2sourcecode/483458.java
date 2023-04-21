    public static void main(String[] args) {
        System.out.println("Starting test NFC Module");
        try {
            TaggerInterface reader = new Arygon();
            ArrayList list = reader.getAvaibleCOMPorts();
            for (Object c : list) {
                System.out.println(c);
            }
            reader.connect("COM4");
            System.out.println(reader.version());
            reader.write("1=bruno,2=amaral,3=montijo");
            System.out.println(reader.read());
            reader.disconnect();
        } catch (Exception exc) {
            System.out.println(exc.getMessage());
        }
    }
