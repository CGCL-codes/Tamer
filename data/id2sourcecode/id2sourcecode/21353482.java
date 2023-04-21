    private void copyFile(InputStream input, OutputStream output) throws Exception {
        log.add("copyFile...");
        byte[] buffer = new byte[100000];
        int read = 0;
        InputStream in = input;
        OutputStream out = output;
        try {
            while (true) {
                read = in.read(buffer);
                if (read == -1) {
                    break;
                }
                out.write(buffer, 0, read);
            }
        } finally {
            if (in != null) {
                try {
                    in.close();
                } finally {
                    if (out != null) {
                        out.close();
                    }
                }
            }
        }
        log.add("...done");
    }
