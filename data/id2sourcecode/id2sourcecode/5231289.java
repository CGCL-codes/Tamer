    private void copyFileToArchive(final ZipArchiveOutputStream zos, final File file, final String dir) {
        try {
            if (file.isFile()) {
                ZipArchiveEntry zae = new ZipArchiveEntry(dir + file.getName());
                zae.setSize(file.length());
                zos.putArchiveEntry(zae);
                MessageDigest md = MessageDigest.getInstance("SHA-1");
                DigestInputStream in = new DigestInputStream(new FileInputStream(file), md);
                byte[] buf = new byte[16384];
                int l = 0;
                while ((l = in.read(buf)) > 0) {
                    zos.write(buf, 0, l);
                    progNow += l;
                    if (Thread.currentThread().isInterrupted()) {
                        zos.closeArchiveEntry();
                        return;
                    }
                    updateProgress();
                }
                Application.getController().displayVerbose("Hash of " + zae.getName() + ": " + new Base64().encodeToString(md.digest()));
                zae.setComment(new Base64().encodeToString(md.digest()));
                zos.closeArchiveEntry();
            } else if (file.isDirectory()) {
                File subs[] = file.listFiles();
                for (int i = 0; i < subs.length; i++) {
                    copyFileToArchive(zos, subs[i], dir + file.getName() + "/");
                }
            }
        } catch (IOException ex) {
            Application.getController().displayError(bundle.getString("error_generic_io"), ex.getLocalizedMessage());
        } catch (NoSuchAlgorithmException ex) {
            Application.getController().displayError(bundle.getString("unknown_alg_text"), ex.getLocalizedMessage());
        }
    }
