    private void uncompress() throws FileNotFoundException, IOException, JixException {
        ZipInputStream zis = new ZipInputStream(UnixInstallerExec.class.getResourceAsStream("/unix-package.zip"));
        ZipEntry zipEntry;
        while ((zipEntry = zis.getNextEntry()) != null) {
            File file = new File(installDir.getPath() + File.separator + zipEntry.getName());
            file.getParentFile().mkdirs();
            FileOutputStream fos = new FileOutputStream(file);
            if (textFiles.contains(zipEntry.getName())) {
                BufferedReader br = new BufferedReader(new InputStreamReader(zis));
                String line;
                while ((line = br.readLine()) != null) fos.write(scapeText(line + "\n").getBytes());
            } else {
                int length;
                while ((length = zis.read(buffer)) != -1) fos.write(buffer, 0, length);
            }
            if (executableFiles.contains(zipEntry.getName())) {
                setExecutable(file);
            }
            fos.close();
            zis.closeEntry();
        }
        zis.close();
    }
