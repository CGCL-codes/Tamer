    public static void main(String args[]) {
        System.out.println("Starting simple test...");
        simple();
        try {
            Thread.sleep(500);
        } catch (InterruptedException ignored) {
        }
        System.out.println("\nStarting reader/writer test...");
        readerWriter();
        try {
            Thread.sleep(1500);
        } catch (InterruptedException ignored) {
        }
        System.out.println("\nStarting multi-reader/writer test...");
        multiReaderWriter();
        try {
            Thread.sleep(1500);
        } catch (InterruptedException ignored) {
        }
        System.out.println("\nStarting dozen-reader/writer test...");
        dozenReaderWriter();
        try {
            Thread.sleep(1500);
        } catch (InterruptedException ignored) {
        }
        System.out.println("\nStarting starving-dozen-reader/writer test...");
        starvingDozenReaderWriter();
        try {
            Thread.sleep(1500);
        } catch (InterruptedException ignored) {
        }
        System.out.println("\nStarting starving-dozen-reader/Multi-writer test (normal close)");
        starvingDozenReaderMultiWriter(false);
        try {
            Thread.sleep(1500);
        } catch (InterruptedException ignored) {
        }
        System.out.println("\nStarting starving-dozen-reader/Multi-writer test (abandon)");
        starvingDozenReaderMultiWriter(true);
        System.out.println("Exiting the test app");
    }
