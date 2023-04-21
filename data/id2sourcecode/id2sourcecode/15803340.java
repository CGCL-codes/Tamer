    public byte[] inputStreamToArrayBytes(InputStream inputStream) {
        System.out.println("DENTRO...!!!");
        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream(1024);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) >= 0) out.write(buffer, 0, len);
            inputStream.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }
