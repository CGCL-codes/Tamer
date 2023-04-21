    private void grepFile(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            FileChannel fc = fis.getChannel();
            int sz = (int) fc.size();
            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, sz);
            Charset charset = Charset.forName("ISO-8859-15");
            CharsetDecoder decoder = charset.newDecoder();
            CharBuffer cb = decoder.decode(bb);
            Pattern linePattern = Pattern.compile(".*\r?\n");
            Pattern pattern = Pattern.compile(m_Request.text());
            Matcher lm = linePattern.matcher(cb);
            Matcher pm = null;
            int nLine = 0;
            while (lm.find()) {
                nLine++;
                CharSequence cs = lm.group();
                if (pm == null) pm = pattern.matcher(cs); else pm.reset(cs);
                if (pm.find()) {
                    TextPoint textElement = new TextPoint(file.getPath(), nLine, cs.toString());
                    m_MainDialog.onTextElementFound(textElement);
                }
                if (lm.end() == cb.limit()) break;
            }
            fis.close();
        } catch (IOException ex) {
            return;
        }
    }
