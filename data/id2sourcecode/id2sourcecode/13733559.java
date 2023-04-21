        public Data process(Data request) {
            String fileName = (String) request.getData();
            File file = new File(sharePath + fileName);
            if (file.exists() && !file.isDirectory()) {
                FileChannel in = null;
                try {
                    in = new FileInputStream(file).getChannel();
                    long size = file.length();
                    ByteBuffer buffer = ByteBuffer.allocate((int) size);
                    in.read(buffer);
                    buffer.flip();
                    FileHolder holder = new FileHolder();
                    holder.setFile(buffer.array(), new FileInfo(file.getName(), file.canRead(), file.length(), file.isDirectory()));
                    return Datas.create(holder);
                } catch (Exception e) {
                    e.printStackTrace();
                    return Datas.create(new FileHolder());
                } finally {
                    try {
                        in.close();
                    } catch (Exception e) {
                    }
                }
            }
            return Datas.create(new FileHolder());
        }
