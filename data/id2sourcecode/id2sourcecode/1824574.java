    public static String readDataFromFile(String filename, Context ctx) {
        String data = null;
        try {
            FileInputStream fin = ctx.openFileInput(filename);
            BufferedInputStream bis = new BufferedInputStream(fin);
            ByteArrayBuffer baf = new ByteArrayBuffer(50);
            int current = 0;
            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }
            data = new String(baf.toByteArray());
        } catch (IOException e) {
            Log.e("AndroidIOUtil.java", "I/O Exception: Error in reading file.");
        }
        return data;
    }
