            public void work(final ByteArrayInputStream in) {
                new ForResource<ByteArrayOutputStream>(new ByteArrayOutputStream()) {

                    public void work(final ByteArrayOutputStream out) {
                        for (int a = 0; a < 3; a++) out.write(in.read());
                        if (!Arrays.equals(original, out.toByteArray())) throw null;
                    }
                };
            }
