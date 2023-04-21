        void activateNextClassFile() {
            try {
                if (zip_file_en == null) {
                    zip_file = new ZipFile(new File(super.in));
                    zip_file_en = zip_file.entries();
                    my_zip_os = new MyZipOutputStream((OutputStream) (new FileOutputStream(new File(super.out))));
                    my_zip_os.setLevel(0);
                }
                do {
                    cur_zip_entry = (ZipEntry) zip_file_en.nextElement();
                    if (!cur_zip_entry.getName().endsWith(".class")) {
                        final int BUF_LEN = 1024;
                        byte b[] = new byte[BUF_LEN];
                        int len;
                        cur_zip_entry.setCompressedSize(-1);
                        my_zip_os.putNextEntry(cur_zip_entry);
                        InputStream is = zip_file.getInputStream(cur_zip_entry);
                        while ((len = is.read(b, 0, BUF_LEN)) > 0) {
                            my_zip_os.write(b, 0, len);
                        }
                    } else {
                        ZipEntry new_zip_entry = new ZipEntry(cur_zip_entry.getName());
                        new_zip_entry.setComment(cur_zip_entry.getComment());
                        new_zip_entry.setExtra(cur_zip_entry.getExtra());
                        my_zip_os.putNextEntry(new_zip_entry);
                    }
                } while (zip_file_en.hasMoreElements() && !cur_zip_entry.getName().endsWith(".class"));
            } catch (Exception e) {
                System.out.println(e.getMessage() + " (in - " + super.in + ", out - " + super.out + ")");
                System.exit(1);
            }
        }
