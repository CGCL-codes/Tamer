    public static void main(String args[]) {
        AudioBuffer buff = new AudioBuffer("X", 1, 100, 1000);
        System.out.println(buff.getChannel(0));
        buff.reset(1, 50, 1000);
        System.out.println(buff.getChannel(0));
        buff.reset(1, 25, 1000);
        System.out.println(buff.getChannel(0));
    }
