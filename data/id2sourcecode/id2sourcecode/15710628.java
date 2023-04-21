    public static byte[] readStream(InputStream is, AtomicReference<Float> percent, int block_size) throws IOException {
        if (is != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(block_size);
            int count = 0;
            int available = is.available();
            if (is instanceof LengthInputStream) {
                available = ((LengthInputStream) is).getLength();
            } else {
                available = is.available();
            }
            byte[] readed = new byte[block_size];
            while (true) {
                int read_bytes = is.read(readed);
                if (read_bytes >= 0) {
                    baos.write(readed, 0, read_bytes);
                    count += read_bytes;
                    if (percent != null) {
                        if (available < count) {
                            available = count + is.available();
                        }
                        percent.set(count / (float) available);
                    }
                } else {
                    break;
                }
            }
            return baos.toByteArray();
        }
        return null;
    }
