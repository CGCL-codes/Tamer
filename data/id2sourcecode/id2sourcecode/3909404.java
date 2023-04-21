    public void testCompress2() throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        DeflaterOutputStream deflater = new DeflaterOutputStream(output, new Deflater(Deflater.BEST_COMPRESSION));
        deflater.write(inputString.getBytes("GBK"));
        deflater.finish();
        deflater.flush();
        byte[] buffer = output.toByteArray();
        System.out.println(buffer.length);
        ByteArrayInputStream input = new ByteArrayInputStream(buffer);
        InflaterInputStream inflater = new InflaterInputStream(input);
        output.reset();
        byte[] temparr = new byte[1024];
        int count = 0;
        while ((count = inflater.read(temparr)) > 0) output.write(temparr, 0, count);
        buffer = output.toByteArray();
        System.out.println(buffer.length);
        System.out.println(new String(buffer, "GBK"));
    }
