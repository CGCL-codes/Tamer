        private void download(DataFile dl) throws IOException, Exception {
            File dst = new File(dl_root, dl.getPath());
            label_file_current = dl.getName();
            if (dl.isDirectory()) {
                progress_overall_current++;
                dst.mkdirs();
                for (DataFile ds : dl.children()) download(ds);
            } else {
                progress_overall_current++;
                dst.getParentFile().mkdirs();
                OutputStream out = new FileOutputStream(dst);
                InputStream in = dl.getInputStream();
                byte[] buff = new byte[0x800];
                int read;
                progress_file_current = 0;
                progress_file_max = dl.size();
                while ((read = in.read(buff)) != -1) {
                    out.write(buff, 0, read);
                    progress_file_current += read;
                    if (stopped) throw new Exception("Thread stopped by user input.");
                }
                in.close();
                out.close();
            }
        }
