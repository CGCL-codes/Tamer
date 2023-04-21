    @Override
    public String generateBundle(FunctionBundleInfo info) throws IOException, Exception {
        String fileName = baseDir + "ezf_" + info.functionName + ".jar";
        String[] jarList = null;
        if (info.loadDir) {
            jarList = getFileList(info.jarFileName);
        } else {
            jarList = new String[] { info.jarFileName };
        }
        String[] classList = getClassList();
        File ctrFile = new File(fileName);
        JarOutputStream outputStream = new JarOutputStream(new FileOutputStream(ctrFile), getManifest(info, jarList));
        byte[] buff = new byte[1024];
        int count;
        String curFileName = "";
        BufferedInputStream curFile;
        for (int i = 0; i < jarList.length; i++) {
            curFileName = jarList[i];
            if (curFileName.startsWith("http")) {
                URL url = new URL(curFileName);
                HttpURLConnection uc = (HttpURLConnection) url.openConnection();
                curFile = new BufferedInputStream(uc.getInputStream());
            } else {
                curFile = new BufferedInputStream(new FileInputStream(curFileName));
            }
            outputStream.putNextEntry(new JarEntry(getOnlyJarName(curFileName)));
            while ((count = curFile.read(buff)) != -1) {
                outputStream.write(buff, 0, count);
            }
        }
        for (int i = 0; i < classList.length; i++) {
            curFileName = classList[i];
            curFile = new BufferedInputStream(this.getClass().getResourceAsStream(curFileName));
            outputStream.putNextEntry(new JarEntry(getOnlyClassName(curFileName)));
            while ((count = curFile.read(buff)) != -1) {
                outputStream.write(buff, 0, count);
            }
        }
        outputStream.close();
        return fileName;
    }
