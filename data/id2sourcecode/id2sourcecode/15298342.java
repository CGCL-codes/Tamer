    private static void jbadd(JarOutputStream _jout, File _dir, String _prefix, int packMode) throws IOException {
        File[] content = _dir.listFiles();
        for (int i = 0, l = content.length; i < l; ++i) {
            if (content[i].isDirectory()) {
                jbadd(_jout, content[i], _prefix + (_prefix.equals("") ? "" : "/") + content[i].getName(), packMode);
            } else {
                boolean canPack = false;
                switch(packMode) {
                    case PACK_ALL_EXCEPT_JAVA_AND_JAR:
                        if (!(content[i].getName().endsWith(".java") || content[i].getName().endsWith(".jar"))) canPack = true;
                        break;
                    case PACK_CLASSES_ONLY:
                        if (content[i].getName().endsWith(".class")) canPack = true;
                        break;
                    case PACK_ALL:
                        canPack = true;
                        break;
                    default:
                        canPack = true;
                }
                if (canPack == true) {
                    _jout.putNextEntry(new ZipEntry(_prefix + "/" + content[i].getName()));
                    FileInputStream in = new FileInputStream(content[i]);
                    jbwrite(in, _jout);
                    in.close();
                }
            }
        }
    }
