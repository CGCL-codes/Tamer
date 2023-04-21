    private void createArchive(OutputStream out) throws Exception {
        try {
            ZipOutputStream zos = new ZipOutputStream(out);
            PEMWriter pw = new PEMWriter(new OutputStreamWriter(zos));
            CertificateArchive arch = RequestUtilities.getCertificateArchive(getServletContext());
            zos.setComment("Certificates archive created on " + new Date().toString());
            List<CertificateInfo> certs = arch.getValidCertificates();
            for (CertificateInfo ci : certs) {
                ZipEntry ze = new ZipEntry(ci.getId() + ".pem");
                if (ci.getIssue().getDate() != null) {
                    ze.setTime(ci.getIssue().getDate().getTime());
                }
                zos.putNextEntry(ze);
                zos.flush();
                pw.writeObject(ci.getCertificate());
                pw.flush();
                zos.closeEntry();
            }
            zos.close();
        } catch (Exception e) {
            logger.error(e, e);
        }
    }
