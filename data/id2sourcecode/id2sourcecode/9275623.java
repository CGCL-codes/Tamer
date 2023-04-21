    private void moveFile(String dirWorkspace, String dirWebServer, String subcarpeta, File fileWorkspace) {
        if (fileWorkspace.isDirectory()) {
            File _fileTemp2;
            for (int indice = 0; indice < fileWorkspace.listFiles().length; indice++) {
                _fileTemp2 = new File(dirWebServer + subcarpeta + Constant.SEPARATOR + fileWorkspace.listFiles()[indice].getName());
                if (fileWorkspace.listFiles()[indice].isDirectory()) {
                    if (!_fileTemp2.exists()) {
                        _fileTemp2.mkdir();
                    }
                    moveFile(dirWorkspace, dirWebServer, subcarpeta + Constant.SEPARATOR + fileWorkspace.listFiles()[indice].getName(), fileWorkspace.listFiles()[indice]);
                } else {
                    if (fileWorkspace.listFiles()[indice].lastModified() > _fileTemp2.lastModified()) {
                        if (copyFile(fileWorkspace.listFiles()[indice], _fileTemp2)) {
                            console.newMessageStream().println("[INFO]  adding " + _fileTemp2.getAbsolutePath().substring(DIRECTORIO_RAIZ));
                        } else {
                            console.newMessageStream().println("[ERROR] adding " + _fileTemp2.getAbsolutePath().substring(DIRECTORIO_RAIZ));
                        }
                    }
                }
            }
            _fileTemp2 = new File(dirWebServer + subcarpeta);
            verificacionFiles(fileWorkspace, _fileTemp2, subcarpeta);
        }
    }
