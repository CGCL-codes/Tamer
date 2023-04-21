    @Test
    public void testReadFile() throws IOException {
        byte[] data = new byte[1024 * 10240];
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) (i % 127);
        }
        File f = new File(tempPath + "read-and-write-file.txt");
        long start = System.currentTimeMillis();
        FileUtil.writeFile(f, data);
        long end = System.currentTimeMillis();
        System.out.println("Write 10MB data: " + (end - start) + " ms.");
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        start = System.currentTimeMillis();
        FileUtil.readFile(f, new BufferedOutputStream(byteArray));
        end = System.currentTimeMillis();
        System.out.println("Read 10MB data: " + (end - start) + " ms.");
        byte[] content = byteArray.toByteArray();
        for (int i = 0; i < data.length; i++) {
            assertEquals(data[i], content[i]);
        }
        assertTrue(FileUtil.removeFile(f));
    }
