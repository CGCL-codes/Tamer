        private void upload(File up, DataFile path) throws IOException, Exception {
            DataFile dst = path.child(up.getName());
            label_file_current = up.getName();
            if (up.isDirectory()) {
                progress_overall_current++;
                dst.mkdirs();
                for (File file : up.listFiles()) upload(file, dst);
            } else {
                progress_overall_current++;
                dst.getParent().mkdirs();
                dst.touch();
                OutputStream out = dst.getOutputStream();
                InputStream in = new FileInputStream(up);
                byte[] buff = new byte[0x800];
                int read;
                progress_file_current = 0;
                progress_file_max = up.length() + 1;
                while ((read = in.read(buff)) != -1) {
                    out.write(buff, 0, read);
                    progress_file_current += read;
                    if (stopped) throw new Exception("Thread stopped by user input.");
                }
                in.close();
                out.close();
            }
        }
