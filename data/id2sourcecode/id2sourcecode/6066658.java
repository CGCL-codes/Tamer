    private byte[] getZippedGemIndex() {
        InputStream content = null;
        byte[] input = new byte[1024];
        int index = 0;
        try {
            URL url = new URL(GEM_INDEX_URL);
            URLConnection con = url.openConnection();
            content = new InflaterInputStream((InputStream) con.getContent());
            while (true) {
                byte[] tmp = new byte[4096];
                int length = content.read(tmp);
                if (length == -1) break;
                while ((index + length) > input.length) {
                    byte[] newInput = new byte[input.length * 2];
                    System.arraycopy(input, 0, newInput, 0, input.length);
                    input = newInput;
                }
                System.arraycopy(tmp, 0, input, index, length);
                index += length;
            }
        } catch (Exception e) {
            AptanaRDTPlugin.log(e);
            return new byte[0];
        } finally {
            try {
                if (content != null) {
                    content.close();
                }
            } catch (IOException e) {
            }
        }
        byte[] newInput = new byte[index];
        System.arraycopy(input, 0, newInput, 0, index);
        return newInput;
    }
