    public String save(String dirPath) throws IOException {
        if (md5 == null) {
            int length = (int) file.length();
            if (length == 0) {
                md5 = "_0";
            } else {
                try {
                    MessageDigest digest = MessageDigest.getInstance("MD5");
                    StringBuilder sb = new StringBuilder("_");
                    for (byte b : digest.digest((file.getPath() + ":" + file.length() + ":" + file.lastModified()).getBytes())) {
                        sb.append(Integer.toHexString(0xFF & b));
                    }
                    md5 = sb.toString();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                    md5 = "_e";
                }
            }
        }
        String fileName = file.getName();
        int i = fileName.lastIndexOf('.');
        if (i > 0) fileName = fileName.substring(0, i);
        fileName += md5;
        if (dirPath == null) dirPath = "";
        if (dirPath.length() > 0 && !dirPath.equals(File.separatorChar)) dirPath += File.separatorChar;
        File file = new File(dirPath + fileName + ".html");
        if (file.isFile()) return file.getAbsolutePath(); else if (file.exists()) {
            fileName = fileName + "_" + System.currentTimeMillis();
            file = new File(dirPath + fileName + ".html");
        }
        if (!isDecode) decode();
        if (content == null) content = "";
        if (entityMap.size() > 1) {
            dirPath = dirPath + fileName + File.separatorChar;
            File dir = new File(dirPath);
            dir.mkdirs();
            dirPath = dir.getName() + "/";
            Iterator<Entry<String, Entity>> iterator = entityMap.entrySet().iterator();
            List<String> entityNameList = new ArrayList<String>();
            while (iterator.hasNext()) {
                Entity entity = iterator.next().getValue();
                int i1 = entity.location.indexOf("?");
                int i2;
                if (i1 > -1) i2 = entity.location.lastIndexOf("/", i1); else {
                    i1 = entity.location.length();
                    i2 = entity.location.lastIndexOf("/");
                }
                String entityName = (i2 > -1 ? entity.location.substring(i2 + 1, i1) : "");
                if (entityName.length() == 0) entityName = String.valueOf(System.currentTimeMillis());
                entityName = entityName.toLowerCase();
                while (entityNameList.contains(entityName)) {
                    entityName = entityName + "_" + System.nanoTime();
                }
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(dir, entityName)));
                out.write(entity.data);
                out.close();
                content = content.replace(entity.location, dirPath + entityName);
            }
        }
        content = content.replaceAll("<base\\s+href\\s*=\\s*\".*\".*((/>)|(</base>))", "");
        if (url != null) content = content.replace(url, file.getName());
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
        out.write(content.getBytes());
        out.close();
        return file.getAbsolutePath();
    }
