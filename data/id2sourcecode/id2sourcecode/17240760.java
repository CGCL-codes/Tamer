    public File pakEntrys(Map<String, byte[]> entrys, String out) {
        try {
            File output = new File(dir, out).getCanonicalFile();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ZipOutputStream zip_out = new ZipOutputStream(baos);
            try {
                for (Entry<String, byte[]> pak : entrys.entrySet()) {
                    ZipEntry entry = new ZipEntry(pak.getKey());
                    try {
                        entry.setTime(0);
                        zip_out.putNextEntry(entry);
                        zip_out.write(pak.getValue());
                    } catch (Exception err) {
                        err.printStackTrace();
                    }
                }
            } finally {
                try {
                    zip_out.close();
                } catch (IOException e) {
                }
                try {
                    baos.close();
                } catch (IOException e) {
                }
            }
            CFile.writeData(output, baos.toByteArray());
            return output;
        } catch (Exception err) {
            err.printStackTrace();
        }
        return null;
    }
