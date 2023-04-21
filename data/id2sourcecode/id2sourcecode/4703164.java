    void showClass(String className) {
        out.println("javac: show class: " + className);
        URL url = getClass().getResource('/' + className.replace('.', '/') + ".class");
        if (url == null) out.println("  class not found"); else {
            out.println("  " + url);
            try {
                final String algorithm = "MD5";
                byte[] digest;
                MessageDigest md = MessageDigest.getInstance(algorithm);
                DigestInputStream in = new DigestInputStream(url.openStream(), md);
                try {
                    byte[] buf = new byte[8192];
                    int n;
                    do {
                        n = in.read(buf);
                    } while (n > 0);
                    digest = md.digest();
                } finally {
                    in.close();
                }
                StringBuilder sb = new StringBuilder();
                for (byte b : digest) sb.append(String.format("%02x", b));
                out.println("  " + algorithm + " checksum: " + sb);
            } catch (Exception e) {
                out.println("  cannot compute digest: " + e);
            }
        }
    }
