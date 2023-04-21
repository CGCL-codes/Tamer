        public void run() {
            try {
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(dest));
                URL url = new URL(remote);
                InputStream is = url.openStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                byte[] buffer = new byte[64535];
                total = 0;
                while (true) {
                    r = bis.read(buffer);
                    if (r == -1) {
                        break;
                    }
                    bos.write(buffer, 0, r);
                    total += r;
                    Thread.sleep(100);
                    if (bStop == true) {
                        break;
                    }
                }
                bis.close();
                bos.close();
            } catch (Exception ex) {
                ExHandler.handle(ex);
            }
        }
