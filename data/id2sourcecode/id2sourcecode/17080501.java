    private XDSDocument writeDocument(BaseDocument doc, XDSDocumentWriter xdsDocWriter) throws IOException, NoSuchAlgorithmException {
        MessageDigest md = null;
        DigestOutputStream dos = null;
        OutputStream out = doc.getOutputStream();
        CountingOutputStream counting = new CountingOutputStream(out);
        if (out != null) {
            log.info("#### Write Document:" + doc.getDocumentUID());
            try {
                md = MessageDigest.getInstance("SHA1");
                dos = new DigestOutputStream(counting, md);
                xdsDocWriter.writeTo(dos);
                log.info("#### File written:" + doc.getDocumentUID());
            } finally {
                if (dos != null) {
                    try {
                        dos.close();
                    } catch (IOException ignore) {
                        log.error("Ignored error during close!", ignore);
                    }
                }
            }
        }
        if (md != null) {
            String hash = DocumentStore.toHexString(md.digest());
            doc.getStorage().setHash(doc, hash);
            return new XDSDocument(doc.getDocumentUID(), doc.getMimeType(), dwFac.getDocumentWriter(doc.getDataHandler(), counting.getCount()), hash, null);
        } else {
            return null;
        }
    }
