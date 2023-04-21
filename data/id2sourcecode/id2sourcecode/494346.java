    public static void main(String args[]) {
        byte blobs[][];
        System.out.println("MyBS Repository Test");
        System.out.println("java.class.path: " + System.getProperty("java.class.path"));
        System.out.println("TEST ITERATIONS: " + ITERATIONS);
        try {
            ContentHandler.register();
            FTP.register();
            HTTP.register();
            int threads = 1;
            for (int i = 0; i < 6; i++) {
                System.out.println("REPOSITORY, THREADS " + threads);
                System.out.print("Upload ");
                blobs = multi_upload_test(threads, ITERATIONS);
                System.out.print("Download ");
                multi_download_test(blobs, threads, ITERATIONS);
                threads *= 2;
            }
            threads = 1;
            for (int i = 0; i < 6; i++) {
                System.out.println("FILE SYSTEM, THREADS " + threads);
                System.out.print("Delete ");
                delete_files();
                System.out.print("Write ");
                multi_write_test(true, threads, ITERATIONS);
                System.out.print("Read ");
                multi_read_test(true, threads, ITERATIONS);
                threads *= 2;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
