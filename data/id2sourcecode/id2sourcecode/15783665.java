        static String calculateMd5(String sequence) {
            String md5;
            synchronized (m) {
                m.reset();
                m.update(sequence.getBytes(), 0, sequence.length());
                md5 = new BigInteger(1, m.digest()).toString(HEXADECIMAL_RADIX);
            }
            return (md5.toLowerCase(Locale.ENGLISH));
        }
