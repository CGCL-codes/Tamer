    private void copyFileFromZip(String realName, ZipInputStream zis) throws FileNotFoundException, IOException {
        logger.fine("Real target:" + realName);
        byte[] buffer = new byte[4096];
        int read = 0;
        File destFile = new File(realName);
        if (!isAscii) {
            File parentFile = destFile.getParentFile();
            if (!parentFile.isDirectory()) {
                parentFile.mkdir();
            }
            FileOutputStream fos = new FileOutputStream(destFile);
            while ((read = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, read);
            }
            fos.close();
        } else {
            BufferedReader br = new BufferedReader(new InputStreamReader(zis));
            PrintWriter bw = new PrintWriter(new FileOutputStream(destFile));
            String line;
            while ((line = br.readLine()) != null) {
                bw.println(line);
            }
            bw.close();
        }
    }
