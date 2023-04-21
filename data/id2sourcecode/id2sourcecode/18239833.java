    public PerfDataBuffer(VmIdentifier vmid) throws MonitorException {
        File f = new File(vmid.getURI());
        String mode = vmid.getMode();
        try {
            FileChannel fc = new RandomAccessFile(f, mode).getChannel();
            ByteBuffer bb = null;
            if (mode.compareTo("r") == 0) {
                bb = fc.map(FileChannel.MapMode.READ_ONLY, 0L, (int) fc.size());
            } else if (mode.compareTo("rw") == 0) {
                bb = fc.map(FileChannel.MapMode.READ_WRITE, 0L, (int) fc.size());
            } else {
                throw new IllegalArgumentException("Invalid mode: " + mode);
            }
            fc.close();
            createPerfDataBuffer(bb, 0);
        } catch (FileNotFoundException e) {
            throw new MonitorException("Could not find " + vmid.toString());
        } catch (IOException e) {
            throw new MonitorException("Could not read " + vmid.toString());
        }
    }
