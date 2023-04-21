    private void writeAtt(File from, File to) throws FileNotFoundException, IOException {
        if (!from.exists()) return;
        FileInputStream fis = new FileInputStream(from);
        FileOutputStream fos = new FileOutputStream(to);
        try {
            fis.getChannel().transferTo(0, from.length(), fos.getChannel());
        } finally {
            fis.close();
            fos.close();
        }
    }
