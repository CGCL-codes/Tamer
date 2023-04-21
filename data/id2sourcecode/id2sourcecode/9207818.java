    public static boolean renameFile(File from_file, File to_file, boolean fail_on_existing_directory, FileFilter file_filter) {
        if (!from_file.exists()) {
            Debug.out("renameFile: source file '" + from_file + "' doesn't exist, failing");
            return (false);
        }
        if (to_file.exists() && (fail_on_existing_directory || from_file.isFile() || to_file.isFile())) {
            Debug.out("renameFile: target file '" + to_file + "' already exists, failing");
            return (false);
        }
        File to_file_parent = to_file.getParentFile();
        if (!to_file_parent.exists()) {
            FileUtil.mkdirs(to_file_parent);
        }
        if (from_file.isDirectory()) {
            File[] files = null;
            if (file_filter != null) {
                files = from_file.listFiles(file_filter);
            } else {
                files = from_file.listFiles();
            }
            if (files == null) {
                return (true);
            }
            int last_ok = 0;
            if (!to_file.exists()) {
                to_file.mkdir();
            }
            for (int i = 0; i < files.length; i++) {
                File ff = files[i];
                File tf = new File(to_file, ff.getName());
                try {
                    if (renameFile(ff, tf, fail_on_existing_directory, file_filter)) {
                        last_ok++;
                    } else {
                        break;
                    }
                } catch (Throwable e) {
                    Debug.out("renameFile: failed to rename file '" + ff.toString() + "' to '" + tf.toString() + "'", e);
                    break;
                }
            }
            if (last_ok == files.length) {
                File[] remaining = from_file.listFiles();
                if (remaining != null && remaining.length > 0) {
                    if (file_filter == null) {
                        Debug.out("renameFile: files remain in '" + from_file.toString() + "', not deleting");
                    } else {
                        return true;
                    }
                } else {
                    if (!from_file.delete()) {
                        Debug.out("renameFile: failed to delete '" + from_file.toString() + "'");
                    }
                }
                return (true);
            }
            for (int i = 0; i < last_ok; i++) {
                File ff = files[i];
                File tf = new File(to_file, ff.getName());
                try {
                    if (!renameFile(tf, ff, false, null)) {
                        Debug.out("renameFile: recovery - failed to move file '" + tf.toString() + "' to '" + ff.toString() + "'");
                    }
                } catch (Throwable e) {
                    Debug.out("renameFile: recovery - failed to move file '" + tf.toString() + "' to '" + ff.toString() + "'", e);
                }
            }
            return (false);
        } else {
            if ((!COConfigurationManager.getBooleanParameter("Copy And Delete Data Rather Than Move")) && from_file.renameTo(to_file)) {
                return (true);
            } else {
                boolean success = false;
                FileInputStream fis = null;
                FileOutputStream fos = null;
                try {
                    fis = new FileInputStream(from_file);
                    fos = new FileOutputStream(to_file);
                    byte[] buffer = new byte[65536];
                    while (true) {
                        int len = fis.read(buffer);
                        if (len <= 0) {
                            break;
                        }
                        fos.write(buffer, 0, len);
                    }
                    fos.close();
                    fos = null;
                    fis.close();
                    fis = null;
                    if (!from_file.delete()) {
                        Debug.out("renameFile: failed to delete '" + from_file.toString() + "'");
                        throw (new Exception("Failed to delete '" + from_file.toString() + "'"));
                    }
                    success = true;
                    return (true);
                } catch (Throwable e) {
                    Debug.out("renameFile: failed to rename '" + from_file.toString() + "' to '" + to_file.toString() + "'", e);
                    return (false);
                } finally {
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (Throwable e) {
                        }
                    }
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (Throwable e) {
                        }
                    }
                    if (!success) {
                        if (to_file.exists()) {
                            to_file.delete();
                        }
                    }
                }
            }
        }
    }
