    public static MetaDataDedupFile writeFile(String path, String fileName, long size) throws IOException, BufferClosedException, HashtableFullException {
        path = path + File.separator + fileName;
        File f = new File(path);
        if (!f.exists()) f.mkdirs(); else {
            throw new IOException("Cannot create vmdk at path " + path + "because path aleady exists");
        }
        long blockL = size / 512;
        int heads = 255;
        int sectors = 63;
        if (size < gb) {
            heads = 64;
            sectors = 32;
        } else if (size < twogb) {
            heads = 128;
            sectors = 32;
        }
        long cylanders = (size - 512) / (heads * sectors * 512);
        if (cylanders < 0) cylanders = 1;
        StringBuffer sb = new StringBuffer(Main.CHUNK_LENGTH);
        sb.append("# Disk DescriptorFile \n");
        sb.append("version=1 \n");
        sb.append("encoding=\"UTF-8\"\n");
        sb.append("CID=" + RandomGUID.getVMDKCID() + "\n");
        sb.append("parentCID=ffffffff\n");
        sb.append("createType=\"vmfs\"\n");
        sb.append("# Extent description\n");
        sb.append("RW " + blockL + " VMFS \"" + fileName + "-flat.vmdk\" 0\n");
        sb.append("# The Disk Data Base\n");
        sb.append("ddb.virtualHWVersion = \"6\"\n");
        sb.append("ddb.uuid = \"" + RandomGUID.getVMDKGUID() + "\"\n");
        sb.append("ddb.geometry.cylinders = \"" + cylanders + "\"\n");
        sb.append("ddb.geometry.heads = \"" + heads + "\"\n");
        sb.append("ddb.geometry.sectors = \"" + sectors + "\"\n");
        sb.append("ddb.adapterType = \"buslogic\"\n");
        MetaDataDedupFile vmd = MetaFileStore.getMF(path + File.separator + fileName + ".vmdk");
        DedupFileChannel ch = vmd.getDedupFile().getChannel();
        ByteBuffer b = ByteBuffer.wrap(new byte[Main.CHUNK_LENGTH]);
        byte[] strB = sb.toString().getBytes();
        b.put(strB);
        vmd.setLength(strB.length, true);
        vmd.getDedupFile().getWriteBuffer(0, true).write(b.array(), 0);
        vmd.getDedupFile().writeCache();
        vmd.sync();
        vmd.getDedupFile().writeCache();
        MetaDataDedupFile vmdk = MetaFileStore.getMF(path + File.separator + fileName + "-flat.vmdk");
        vmdk.setLength(size, true);
        vmdk.getIOMonitor().setActualBytesWritten(0);
        vmdk.getIOMonitor().setBytesRead(0);
        vmdk.getIOMonitor().setDuplicateBlocks(0);
        vmdk.sync();
        ch.close();
        SDFSLogger.getLog().info("Created vmdk of size " + vmdk.length() + " at " + path + File.separator + fileName);
        return vmdk;
    }
