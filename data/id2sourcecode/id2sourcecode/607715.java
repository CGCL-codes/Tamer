    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("��;��copy���µ�WIAS������");
            System.err.println("Usage:CpoyFiles fromPath toPath");
            System.err.println("      CpoyFiles \\10.144.66.201\\release C:\\V3R2");
            return;
        }
        CopyFiles cf = new CopyFiles();
        File src = new File(args[0]);
        if (!src.isDirectory()) {
            System.out.println("·����Ч��");
            return;
        }
        int srcFolder = 0;
        long temp = 0;
        File srcFiles[] = src.listFiles();
        if (srcFiles.length == 0) {
            return;
        }
        for (int i = 0; i < srcFiles.length; i++) {
            if (srcFiles[i].isFile()) {
                continue;
            }
            if (temp < srcFiles[i].lastModified()) {
                temp = srcFiles[i].lastModified();
                srcFolder = i;
            }
        }
        String srcWIASPath = args[0] + "\\" + srcFiles[srcFolder].getName() + "\\WIAS";
        String dstWIASPath = args[1] + "\\" + srcFiles[srcFolder].getName() + "\\WIAS";
        File srcWias = new File(srcWIASPath);
        if (!srcWias.exists()) {
            System.out.println("������WIASĿ¼");
            return;
        }
        File dstFolder = new File(args[1] + "/" + srcFiles[srcFolder].getName());
        if (!dstFolder.exists() || dstFolder.lastModified() < temp) {
            cf.copyFolder(srcWIASPath, dstWIASPath);
            dstFolder.setLastModified(temp);
            System.out.println(new Date() + " copy�ļ���Ŀ" + cf.getCopyCnt());
            return;
        }
        System.out.println("�������ļ�û�и��£�");
    }
