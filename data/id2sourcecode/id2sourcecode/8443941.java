    public Object doInJcr(Session session) throws IOException, RepositoryException {
        Node resourceNode = session.getNodeByUUID(jcrResource.getUuid());
        InputStream in = resourceNode.getProperty(JcrConstants.JCR_DATA_PROPERTY).getStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream(BUF_SIZE);
        try {
            byte[] buf = new byte[HDD_SECTOR_SIZE];
            int read = 0;
            while ((read = in.read(buf)) != -1) {
                out.write(buf, 0, read);
            }
        } finally {
            in.close();
            out.close();
        }
        return out.toByteArray();
    }
