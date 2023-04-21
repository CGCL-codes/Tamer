        @Override
        public void run() {
            try {
                connect();
                InputStream tmpIn = getSocket().getInputStream();
                SoldatInputStream in = new SoldatInputStream(tmpIn);
                OutputStream tmpOut = getSocket().getOutputStream();
                SoldatOutputStream out = new SoldatOutputStream(tmpOut);
                String filename = FileExchangeHelper.readRequest(in);
                FileInputStream fin = new FileInputStream(filename);
                ByteArrayOutputStream tmp = new ByteArrayOutputStream();
                byte[] buf = new byte[1000];
                int read;
                while ((read = fin.read(buf)) != -1) {
                    tmp.write(buf, 0, read);
                }
                fin.close();
                FileExchangeHelper.writeRequestResponse(out, filename, tmp.toByteArray());
                in.close();
                out.close();
                disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
