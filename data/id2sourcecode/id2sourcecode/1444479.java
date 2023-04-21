    public static void main(String[] args) {
        StopWatch sw = new StopWatch();
        sw.start();
        FileUtils.copyFileIO("C:\\java\\backup\\ZipsAndExes\\jdk-1_5_0_05-windows-i586-p.exe", "C:\\java\\jdk-1_5_0_05-windows-i586-p.exe");
        sw.stop();
        System.out.println("Java IO file Copy: " + sw.getTime());
        sw = new StopWatch();
        sw.start();
        FileUtils.copyFileNIO("C:\\java\\backup\\ZipsAndExes\\jdk-1_5_0_05-windows-i586-p.exe", "C:\\java\\jdk-1_5_0_05-windows-i586-p.exe");
        sw.stop();
        System.out.println("Java NIO file Copy: " + sw.getTime());
        sw = new StopWatch();
        sw.start();
        FileUtils.copyFileSys("C:\\java\\backup\\ZipsAndExes\\jdk-1_5_0_05-windows-i586-p.exe", "C:\\java\\jdk-1_5_0_05-windows-i586-p.exe");
        sw.stop();
        System.out.println("System file copy: " + sw.getTime());
    }
