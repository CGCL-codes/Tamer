        boolean hasMoreClassFiles() {
            if (zip_file_en == null) return true;
            if (!zip_file_en.hasMoreElements()) return false;
            Enumeration tmp_zip_file_en = zip_file.entries();
            ZipEntry tmp_zip_entry;
            do {
                tmp_zip_entry = (ZipEntry) tmp_zip_file_en.nextElement();
            } while (!tmp_zip_entry.getName().equals(cur_zip_entry.getName()));
            do {
                tmp_zip_entry = (ZipEntry) tmp_zip_file_en.nextElement();
                if (tmp_zip_entry.getName().endsWith(".class")) break;
                try {
                    cur_zip_entry = (ZipEntry) zip_file_en.nextElement();
                    final int BUF_LEN = 1024;
                    byte b[] = new byte[BUF_LEN];
                    int len;
                    cur_zip_entry.setCompressedSize(-1);
                    my_zip_os.putNextEntry(cur_zip_entry);
                    InputStream is = zip_file.getInputStream(cur_zip_entry);
                    while ((len = is.read(b, 0, BUF_LEN)) > 0) {
                        my_zip_os.write(b, 0, len);
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    System.exit(1);
                }
            } while (tmp_zip_file_en.hasMoreElements() && !tmp_zip_entry.getName().endsWith(".class"));
            if (tmp_zip_entry.getName().endsWith(".class")) return true;
            return false;
        }
