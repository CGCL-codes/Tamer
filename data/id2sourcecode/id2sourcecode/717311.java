    public static void main(String[] args) {
        BlueSlider bs;
        if (args.length == 0) bs = new BlueSlider("iw-room2", "COM3"); else if (args.length == 1) bs = new BlueSlider(args[0], "COM3"); else bs = new BlueSlider(args[0], args[1]);
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            try {
                bs.getOutputStream().write(in.readLine().getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
