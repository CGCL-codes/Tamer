        public void close() throws IOException {
            int tag = input.read();
            if (tag != 0xD3) throw new SyntaxException(Packet.TAG_ERROR + " 0x" + Integer.toHexString(tag & 0xFF));
            digest.update((byte) tag);
            digest.update((byte) input.read());
            byte[] calc = digest.digest();
            int i = calc.length;
            for (i = 0; i < calc.length; i++) if (calc[i] != (byte) input.read()) throw new MDCException("Bad MDC");
            input.close();
            super.close();
        }
