        public void extract(ZipEntry ze) throws IOException {
            File d = null;
            InputStream in = zf.getInputStream(ze);
            d = new File(dest, ze.getName());
            if (ze.isDirectory() && !d.exists()) d.mkdir(); else if (d.isFile() && d.exists()) {
                d.getParentFile().mkdirs();
                if (isoverwrite == NoToAll) return;
                if (isoverwrite != YesToAll) {
                    JLabel l = new JLabel(JZipWizard.getString("ExtractOverWriteText"));
                    l.setUI(new MultiLineLabelUI());
                    l.setFont(JMOSI.getDefaultFont());
                    Object[] options = { JZipWizard.getString("ExtractOverWriteYes"), JZipWizard.getString("ExtractOverWriteYesToAll"), JZipWizard.getString("ExtractOverWriteNo"), JZipWizard.getString("ExtractOverWriteNoToAll") };
                    isoverwrite = JOptionPane.showOptionDialog(parent, l, JZipWizard.getString("ExtractOverWriteTitle"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                    switch(isoverwrite) {
                        case No:
                        case NoToAll:
                            return;
                    }
                    d.createNewFile();
                }
            }
            FileOutputStream out;
            out = new FileOutputStream(d);
            byte[] buf = new byte[1024];
            int readed = 0;
            while ((readed = in.read(buf)) > 0) {
                out.write(buf, 0, readed);
            }
            out.close();
            in.close();
        }
