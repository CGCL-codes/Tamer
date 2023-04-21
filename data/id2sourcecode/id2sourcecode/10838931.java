    private void ZipImageFtps(ZipOutputStream out) throws Exception {
        if (img_ftp_hosts == null || img_ftp_hosts.isEmpty()) return;
        String filename = "SITE" + GetId() + ".iftp";
        out.putNextEntry(new ZipEntry(filename));
        try {
            ZipWriter writer = new ZipWriter(out);
            for (Object obj : img_ftp_hosts) {
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
