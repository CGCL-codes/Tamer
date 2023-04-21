    public void writeEntry(JarFile jarFile, JarEntry entry) {
        String name = entry.getName();
        name = name.replace('/', File.separatorChar);
        File file = new File(tempDir, name);
        File dir = file.getParentFile();
        if (!dir.exists()) {
            boolean success = dir.mkdirs();
            if (!success) alert("Error occured while unpack distribution:\nCreating directory\n" + dir.getAbsolutePath() + "\nfailed !");
        }
        try {
            BufferedInputStream bis = new BufferedInputStream(jarFile.getInputStream(entry));
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            if (consoleMode) System.out.print(".");
            int read;
            while ((read = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, read);
                if (progressBar != null) progressBar.setValue((bytesWritten += read));
            }
            bis.close();
            bos.close();
        } catch (Exception e) {
            alert("Error occured while unpack distribution:\nCopying resource\n" + entry.getName() + "\nto directory\n" + dir.getAbsolutePath() + "\nfailed !");
        }
    }
