        public static void zip(List<ArchiveEntry> entries, File file, boolean b) {
            try {
                ZipOutputStream jos = new ZipOutputStream(new FileOutputStream(file));
                jos.setLevel(9);
                for (ArchiveEntry entry : entries) {
                    jos.putNextEntry(new ZipEntry(entry.name));
                    Streams.transfer(new FileInputStream(entry.file), jos);
                    jos.closeEntry();
                }
                jos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
