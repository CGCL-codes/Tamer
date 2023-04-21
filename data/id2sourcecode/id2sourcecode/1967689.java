    public boolean saveAs(String context, String path, InputStream source) {
        HttpURLConnection conn;
        try {
            conn = (HttpURLConnection) (new URL(url)).openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            OutputStream os = conn.getOutputStream();
            HessianOutput hso = new HessianOutput(os);
            hso.startCall("saveAs");
            hso.writeString(context);
            hso.writeString(path);
            hso.writeByteBufferStart();
            byte buf[] = new byte[1024];
            do {
                int read = source.read(buf);
                if (read == -1) break;
                if (read != 0) hso.writeByteBufferPart(buf, 0, read);
            } while (true);
            hso.writeByteBufferEnd(buf, 0, 0);
            hso.completeCall();
            hso.flush();
            hso.close();
            os.close();
            boolean result;
            InputStream is = conn.getInputStream();
            HessianInput hsi = new HessianInput(is);
            hsi.startReply();
            result = hsi.readBoolean();
            hsi.completeReply();
            return result;
        } catch (Throwable ex) {
            ex.printStackTrace();
            return false;
        }
    }
