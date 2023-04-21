        public void compressDir(File f, String parentPath) throws IOException {
            if (!f.isDirectory()) return;
            String name = f.getAbsolutePath();
            name.replaceAll("\\\\", "/");
            if (name.startsWith(parentPath)) {
                name = name.substring(parentPath.length());
            } else name = f.getName();
            if (!name.startsWith("/")) {
                name = name.substring(1);
            }
            if (!name.endsWith("/")) {
                name += "/";
            }
            message = parent.getName() + " : " + name;
            ZipEntry entry = new ZipEntry(name);
            dest.putNextEntry(entry);
            entry.setTime(f.lastModified());
            dest.closeEntry();
            File list[] = f.listFiles();
            int i = 0;
            for (; i < list.length; i++) {
                if (list[i].isDirectory()) compressDir(list[i], parentPath); else {
                    name = list[i].getAbsolutePath();
                    name.replaceAll("\\\\", "/");
                    if (name.startsWith(parentPath)) {
                        name = name.substring(parentPath.length());
                    } else name = list[i].getName();
                    if (name.startsWith("\\")) {
                        name = name.substring(1);
                    }
                    message = parent.getName() + " : " + name;
                    compress(list[i], name);
                }
            }
        }
