        public String doInBackground() {
            int k;
            Long l, s;
            FileInputStream fins;
            MessageDigest md;
            try {
                byte[] buf = new byte[128 * 1024];
                setProgress(0);
                md = MessageDigest.getInstance(h);
                fins = new FileInputStream(f);
                s = f.length();
                l = 0L;
                do {
                    k = fins.read(buf);
                    if (k > 0) {
                        md.update(buf, 0, k);
                        l += k;
                        setProgress((int) (l * 100 / s));
                    }
                } while (k > 0);
                fins.close();
                h = arrayBytetohexString(md.digest());
                succeed = true;
            } catch (Exception e) {
                h = e.getMessage();
            }
            return h;
        }
