        public static String saveFile(String _filename, InputStream _input) throws IOException {
            if (_filename == null) _filename = File.createTempFile("xx", ".out").getName();
            File file = new File(c_dir + _filename);
            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            BufferedInputStream bis = new BufferedInputStream(_input);
            int aByte;
            while ((aByte = bis.read()) != -1) bos.write(aByte);
            bos.flush();
            bos.close();
            bis.close();
            return (c_dir + _filename);
        }
