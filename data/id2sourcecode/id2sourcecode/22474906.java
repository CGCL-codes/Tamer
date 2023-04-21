    public static boolean writeFile(String pathfile, String s2) {
        try {
            String strPath = pathfile.substring(0, pathfile.lastIndexOf(File.separator));
            File path = new File(strPath);
            path.mkdir();
            File file = new File(pathfile);
            byte[] src = s2.getBytes("UTF-8");
            int length = src.length;
            FileOutputStream fis = new FileOutputStream(file);
            FileChannel fc = fis.getChannel();
            ByteBuffer bb = ByteBuffer.allocate(length);
            bb.put(src);
            bb.flip();
            fc.write(bb);
            bb.clear();
            fc.close();
            fis.close();
            return true;
        } catch (Exception exception) {
            System.out.println(exception);
            return false;
        }
    }
