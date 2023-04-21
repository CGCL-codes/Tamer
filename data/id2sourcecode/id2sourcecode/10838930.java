    private void ZipArticleFtps(ZipOutputStream out) throws Exception {
        if (article_ftp_hosts == null || article_ftp_hosts.isEmpty()) return;
        String filename = "SITE" + GetId() + ".aftp";
        out.putNextEntry(new ZipEntry(filename));
        try {
            ZipWriter writer = new ZipWriter(out);
            for (Object obj : article_ftp_hosts) {
                FtpHost ahost = (FtpHost) obj;
                writer.println(ahost.GetHostname());
                writer.println(ahost.GetRemotedir());
                writer.println(ahost.GetRemoteport());
                writer.println(ahost.GetUsername());
                writer.println(ahost.GetUserpassword());
            }
        } finally {
            out.closeEntry();
        }
    }
