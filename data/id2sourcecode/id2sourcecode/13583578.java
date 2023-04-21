    public static long transfer(InputStream in, OutputStream out, long maxCount) throws IOException {
        CheckArg.maxCount(maxCount);
        if (maxCount == 0) return 0; else if (in instanceof XDataInput) return ((XDataInput) in).transferTo(out, maxCount); else if (out instanceof XDataOutput) return ((XDataOutput) out).transferFrom(in, maxCount); else {
            long count = 0;
            for (int b; (count != maxCount) && ((b = in.read()) >= 0); ) {
                out.write(b);
                count++;
            }
            return count;
        }
    }
