    private void extractEntry(ZipFile zf, ZipEntry entry, String destDir) throws IOException {
        final byte[] buffer = new byte[0xFFFF];
        File file = new File(destDir, entry.getName());
        if (entry.isDirectory()) file.mkdirs(); else {
            new File(file.getParent()).mkdirs();
            InputStream is = null;
            OutputStream os = null;
            try {
                is = zf.getInputStream(entry);
                os = new FileOutputStream(file);
                for (int len; (len = is.read(buffer)) != -1; ) os.write(buffer, 0, len);
            } finally {
                if (os != null) os.close();
                if (is != null) is.close();
            }
        }
    }
