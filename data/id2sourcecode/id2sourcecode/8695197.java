        public void modifyInitedFile(HashMap<String, String> btnInfo, String filePath) {
            try {
                RandomAccessFile raf = new RandomAccessFile(filePath, "rw");
                raf.setLength(0);
                raf.close();
                FileOutputStream fos = new FileOutputStream(new File(filePath));
                Iterator it = btnInfo.keySet().iterator();
                while (it.hasNext()) {
                    String key = (String) it.next();
                    key = key + "\n";
                    fos.write(key.getBytes());
                }
                fos.getChannel().force(true);
                fos.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
