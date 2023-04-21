        synchronized void rollLog() {
            byte buff[] = new byte[16];
            ByteBuffer bb = ByteBuffer.wrap(buff);
            bb.putLong(txnLogId);
            bb.putLong(txnLogPosition);
            for (File dir : ledgerDirectories) {
                File file = new File(dir, "lastMark");
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(buff);
                    fos.getChannel().force(true);
                    fos.close();
                } catch (IOException e) {
                    LOG.error("Problems writing to " + file, e);
                }
            }
        }
