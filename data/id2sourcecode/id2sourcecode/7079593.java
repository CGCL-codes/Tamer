    @Test
    public void should_copy_file_using_new_io() throws IOException {
        FileChannel src = is.getChannel();
        assertEquals(0, src.position());
        FileChannel target = os.getChannel();
        long l = src.transferTo(0, src.size(), target);
        assertEquals(l, src.size());
        src.close();
        target.close();
        is.close();
        os.close();
    }
