    protected void jbSelectMarcActionPerformed(ActionEvent evt) {
        if (jfcBinMarc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                File f = jfcBinMarc.getSelectedFile();
                FileInputStream fis = new FileInputStream(f);
                FileChannel fch = fis.getChannel();
                ByteArrayOutputStream baos = new ByteArrayOutputStream((int) f.length());
                WritableByteChannel wbc = Channels.newChannel(baos);
                long pos = 0;
                long cnt = 0;
                while ((cnt = fch.transferTo(pos, f.length(), wbc)) > 0) {
                    pos += cnt;
                }
                fis.close();
                this.bmarc = baos.toByteArray();
                int lines = (int) Math.ceil((double) bmarc.length / TEXT_WIDTH);
                StringBuffer sb = new StringBuffer();
                System.out.println(bmarc.length);
                for (int i = 0; i < lines; i++) {
                    sb.append(new String(bmarc, i * TEXT_WIDTH, ((i + 1) * TEXT_WIDTH < bmarc.length) ? (TEXT_WIDTH) : (bmarc.length - i * TEXT_WIDTH), "US-ASCII"));
                    if (i < lines) {
                        sb.append("\n");
                    }
                }
                this.jtpBinaryMarc.setText(sb.toString());
            } catch (FileNotFoundException fnfex) {
            } catch (IOException ioex) {
            }
        }
    }
