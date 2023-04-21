    public static void zipFile(File file, ZipOutputStream zipout, String parentDir) throws FileNotFoundException, IOException {
        if (file.isDirectory()) {
            logger.info(TAG + ": Le fichier " + file.getName() + " est un r�pertoire.");
            String finalFileName;
            if (parentDir == null) finalFileName = file.getName(); else finalFileName = parentDir + SYSTEM_SEP + file.getName();
            ZipEntry entry = new ZipEntry(finalFileName + "/");
            zipout.putNextEntry(entry);
            logger.info(TAG + ": Ajout de l'entr�e " + entry.getName());
            File[] dirFiles = file.listFiles();
            for (int i = 0; i < dirFiles.length; i++) zipFile(dirFiles[i], zipout, finalFileName);
            zipout.closeEntry();
        } else {
            byte data[] = new byte[BUFFER];
            FileInputStream src = new FileInputStream(file);
            BufferedInputStream in = new BufferedInputStream(src, BUFFER);
            String dir;
            if (parentDir == null) dir = ""; else dir = parentDir + SYSTEM_SEP;
            logger.info(TAG + ": Valeur du r�pertoire parent : " + dir);
            ZipEntry entry = new ZipEntry(dir + file.getName());
            zipout.putNextEntry(entry);
            logger.info(TAG + ": Ajout de l'entr�e " + entry.getName());
            int count;
            while ((count = in.read(data, 0, BUFFER)) != -1) zipout.write(data, 0, count);
            zipout.closeEntry();
            in.close();
        }
    }
