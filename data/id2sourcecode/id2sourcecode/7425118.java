    public static void main(String[] args) {
        try {
            File fraf = new File("e:\\fraf.bin");
            File fchan = new File("e:\\fchan.bin");
            File fmapd = new File("e:\\fmapd.bin");
            RandomAccessFile raf = new RandomAccessFile(fraf, "rw");
            FileChannel fc = new RandomAccessFile(fchan, "rw").getChannel();
            MemoryMappedFile mmf = new MemoryMappedFile(fmapd);
            mmf.setAccessMode(MemoryMappedFile.MODE_READ_WRITE);
            long written = 0;
            long traf = 0;
            long tchan = 0;
            long tmmf = 0;
            int loop = 1;
            while (written < MAX_SIZE) {
                System.out.print("|");
                if (loop % 80 == 0) System.out.println();
                refreshBuffers();
                long start_pos = new Float(RandomUtils.nextFloat() * (MAX_SIZE - BUFF_SIZE)).longValue();
                long start = System.currentTimeMillis();
                traf += System.currentTimeMillis() - start;
                start = System.currentTimeMillis();
                tchan += System.currentTimeMillis() - start;
                start = System.currentTimeMillis();
                mmf.write(dbb, 0, start_pos, dbb.limit(DirectByteBuffer.SS_OTHER));
                tmmf += System.currentTimeMillis() - start;
                written += raw.length;
                loop++;
            }
            System.out.println();
            System.out.println("RandomAccessFile = " + traf);
            System.out.println("FileChannel = " + tchan);
            System.out.println("MemoryMappedFile = " + tmmf);
            System.out.println("Cache H: " + MemoryMappedFile.cache_hits + " M: " + MemoryMappedFile.cache_misses);
        } catch (Throwable t) {
            Debug.printStackTrace(t);
        }
    }
