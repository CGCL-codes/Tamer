    public static void main(String[] args) {
        System.out.println("E:" + StrictMath.E);
        System.out.println("Pi:" + StrictMath.PI);
        try {
            System.out.println(Runtime.getRuntime().exec("cmd /c dir ").getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(new File(".").getAbsoluteFile());
    }
