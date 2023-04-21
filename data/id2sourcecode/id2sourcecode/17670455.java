        private String export(String mhtPath, String exportDir) throws Exception {
            File mht = new File(mhtPath);
            String fileName = mht.getName();
            int index = fileName.lastIndexOf('.');
            if (index > 0) fileName = fileName.substring(0, index);
            MessageDigest digest = MessageDigest.getInstance("MD5");
            StringBuilder sb = new StringBuilder("_");
            for (byte b : digest.digest((mht.getPath() + ":" + mht.length() + ":" + mht.lastModified()).getBytes())) {
                sb.append(Integer.toHexString(0xFF & b));
            }
            fileName += sb.toString();
            if (exportDir == null) exportDir = "";
            if (exportDir.length() > 0 && !exportDir.equals(File.separatorChar)) exportDir += File.separatorChar;
            file = new File(exportDir + fileName + ".html");
            if (file.isFile()) return file.getAbsolutePath(); else if (file.exists()) {
                fileName = fileName + "_" + System.currentTimeMillis();
                file = new File(exportDir + fileName + ".html");
            }
            exportDir = exportDir + fileName + File.separatorChar;
            dir = new File(exportDir);
            BufferedReader in = null;
            try {
                in = new BufferedReader(new FileReader(mht));
                String temp;
                while ((temp = in.readLine()) != null) {
                    int i = temp.indexOf("boundary");
                    if (i > -1) {
                        int start = temp.indexOf("\"", i);
                        int end = 0;
                        if (start > -1) {
                            start++;
                            end = temp.indexOf("\"", start);
                        } else {
                            start = temp.indexOf("=", i);
                            start++;
                            end = temp.length();
                        }
                        boundary = temp.substring(start, end);
                        break;
                    }
                }
                splitEntity(in, boundary);
            } finally {
                if (in != null) try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (content == null) {
                throw new Exception("mht format error");
            }
            Iterator<Entry<String, String>> iterator = replaceMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry<String, String> entry = iterator.next();
                content = content.replace(entry.getKey(), entry.getValue());
            }
            content = content.replaceAll("<base\\s+href\\s*=\\s*\".*\".*((/>)|(</base>))", "");
            if (url != null) content = content.replace(url, file.getName());
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
            out.write(content.getBytes());
            out.close();
            return file.getAbsolutePath();
        }
