    public final void verify() throws IOException {
        File[] files = dataDirectory.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".partial");
            }
        });
        for (File f : files) {
            String nm = f.getName();
            long startingOffset = Long.parseLong(nm.substring(nm.indexOf("_0x") + 3, nm.lastIndexOf('.')), 16);
            FileChannel fc_ = new FileInputStream(f).getChannel();
            FileChannel fc = new FileInputStream(completeFile).getChannel().position(startingOffset);
            ByteBuffer buffer = ByteBuffer.allocate((int) fc_.size());
            ByteBuffer buffer_ = ByteBuffer.allocate((int) fc_.size());
            fc_.read(buffer_);
            fc.read(buffer);
            System.out.println("file=" + nm);
            BoundaryConditions.printContentPeek(buffer, buffer_);
            System.out.println("buffersize=" + buffer.capacity() + " filesize=" + f.length());
            assert (buffer.equals(buffer_));
            fc.close();
            fc_.close();
        }
    }
