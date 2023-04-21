    public static void copyFileToZipStream(File zipsource, String name, File rel_path, ZipOutputStream zipoutputstream) throws Exception {
        if (zipsource.isDirectory()) {
            File zipfile = zipsource;
            String source_list[] = zipfile.list();
            for (int i = 0; i < source_list.length; i++) {
                String sourcefilename = source_list[i];
                File sourcefile = new File(zipfile, sourcefilename);
                String relativePathname = getRelativePathname(rel_path, sourcefile);
                copyFileToZipStream(sourcefile, relativePathname, rel_path, zipoutputstream);
            }
        } else {
            FileInputStream fileinputstream = new FileInputStream(zipsource);
            String s1 = name.replace(File.separatorChar, '/');
            ZipEntry zipentry = new ZipEntry(s1);
            zipentry.setSize(zipsource.length());
            zipoutputstream.putNextEntry(zipentry);
            copyStreams(fileinputstream, zipoutputstream);
            zipoutputstream.closeEntry();
            fileinputstream.close();
        }
    }
