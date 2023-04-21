    private byte[] getStreamed(RetrieveOutcomeReply ror, String endOfName) {
        if (ror.hasStreamed()) {
            try {
                PacketisedInputStream pis = new PacketisedInputStream(socket.getInputStream());
                ZipInputStream zis = new ZipInputStream(pis);
                ZipEntry entry = zis.getNextEntry();
                while (entry != null) {
                    if (entry.getName().endsWith(endOfName)) {
                        ByteArrayOutputStream bOutputStream = new ByteArrayOutputStream();
                        while (zis.available() > 0) {
                            int read = zis.read();
                            if (zis.available() > 0) bOutputStream.write(read);
                        }
                        bOutputStream.flush();
                        zis.closeEntry();
                        zis.close();
                        pis.close();
                        return bOutputStream.toByteArray();
                    }
                    zis.closeEntry();
                    entry = zis.getNextEntry();
                }
                zis.close();
                pis.close();
                return null;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
                return null;
            }
        } else return null;
    }
