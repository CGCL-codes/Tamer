    public static void main(String[] args) {
        PureJavaCrc32 crc = new PureJavaCrc32();
        crc.update(new byte[100], 0, 88);
        System.out.println("crc: " + crc.getValue());
        LinuxMemoryCalculatorPlugin calculator = new LinuxMemoryCalculatorPlugin();
        System.out.println("Phycical memory:" + calculator.getPhysicalMemorySize() + " bytes");
    }
