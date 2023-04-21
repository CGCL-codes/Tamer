    public static boolean createFile(String tpl, String newFileName, Map<String, String> map) {
        return NIOUtil.write(new File(newFileName), replaceParams(FileUtil.read(new File(tpl)), map));
    }
