    private void assertEqualFiles(String filename1, String filename2) throws IOException {
        File file1 = new File(filename1);
        File file2 = new File(filename2);
        System.out.printf("Comparing the files %s and %s.%n", file1.getAbsoluteFile(), file2.getAbsoluteFile());
        FileInputStream fileStream1 = new FileInputStream(file1);
        FileInputStream fileStream2 = new FileInputStream(file2);
        FileChannel channel1 = fileStream1.getChannel();
        FileChannel channel2 = fileStream2.getChannel();
        assertEquals(String.format("The files %s and %s have different length.", filename1, filename2), file1.length(), file2.length());
        ByteBuffer buffer1 = ByteBuffer.allocate(file1.length() > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) file1.length());
        ByteBuffer buffer2 = ByteBuffer.allocate(file2.length() > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) file2.length());
        int pos1 = 1;
        int pos2 = 1;
        while (pos1 == pos2 && pos1 > 0) {
            System.out.printf("Comparing position %d.%n", pos1);
            pos1 = channel1.read(buffer1);
            pos2 = channel2.read(buffer2);
            assertEquals(String.format("The files %s and %s have different content.", filename1, filename2), 0, buffer1.compareTo(buffer2));
        }
        assertEquals(String.format("Reading the files %s and %s had end at different positions.", filename1, filename2), pos1, pos2);
        channel1.close();
        channel2.close();
        fileStream1.close();
        fileStream2.close();
    }
