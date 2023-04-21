    public List<EditOperation> processOperations(List<EditOperation> operations, Map<String, StreamDataSource> dss, boolean closeDSSs) throws FileNotFoundException, IOException {
        if (getOutputFileName() == null) {
            return null;
        }
        if (operations == null || operations.size() == 0) {
            return operations;
        }
        FragmentedFileInfo ffi = buildFFI(operations, dss);
        try {
            System.out.println("\nBuilding fragments, final info [" + ffi.getLength() + "]:");
            for (Fragment f : ffi.getFragments()) {
                System.out.println(f.toString());
            }
            ffi.defragment();
            System.out.println("\nBuilding fragments, final info (defragmented) [" + ffi.getLength() + "]:");
            for (Fragment f : ffi.getFragments()) {
                System.out.println(f.toString());
            }
        } catch (IOException ioex) {
        }
        OutputStream fos = null;
        File f = new File(getOutputFileName());
        if (f.exists()) {
            f.delete();
        }
        fos = new BufferedOutputStream(new FileOutputStream(getOutputFileName()), 1024 * 30);
        for (Fragment fr : ffi.getFragments()) {
            lastProcessingFragment = new ProcessingStatus();
            lastProcessingFragment.fr = fr;
            if (fr.eop != null) {
                fr.eop.setStatus(EditOperation.OPERATION_STATUS.executing);
            }
            if (fr.size > 0) {
                InputStream frIs = (fr.eop != null) ? dss.get(fr.eop.getSource()).getInputStream(fr.sourceOffset) : new ByteArrayInputStream(fr.data);
                byte[] buf = new byte[1024 * 4];
                long toReadSize = fr.size;
                while (toReadSize > 0) {
                    int readCount = frIs.read(buf, 0, (int) Math.min(buf.length, toReadSize));
                    toReadSize -= readCount;
                    fos.write(buf, 0, readCount);
                    lastProcessingFragment.length = fr.offset + (fr.size - toReadSize);
                }
            } else {
            }
            if (fr.eop != null) {
                fr.eop.setStatus(EditOperation.OPERATION_STATUS.OK);
            }
        }
        fos.close();
        if (closeDSSs) {
            for (String source : dss.keySet()) {
                try {
                    dss.get(source).close();
                } catch (IOException ioex) {
                }
            }
        }
        return operations;
    }
