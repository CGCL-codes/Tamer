    protected void updateFlowTab() {
        try {
            File tempfile = File.createTempFile("MogwaiChart", ".dot");
            PrintWriter pw = new PrintWriter(new FileWriter(tempfile));
            pw.println(this.m_sourceText.getText());
            pw.flush();
            pw.close();
            BufferedReader br = new BufferedReader(new InputStreamReader(MogwaiChart.class.getClassLoader().getResourceAsStream("imagelist.txt")));
            while (br.ready()) {
                String line = br.readLine();
                this.m_logger.info("Copy resource to temp directory -> " + line);
                byte buffer[] = new byte[8192];
                BufferedInputStream bis = new BufferedInputStream(MogwaiChart.class.getClassLoader().getResourceAsStream(line));
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(tempfile.getParent(), line)));
                while (bis.available() > 0) {
                    int read = bis.read(buffer);
                    bos.write(buffer, 0, read);
                }
                bis.close();
                bos.close();
            }
            br.close();
            File tempfile2 = File.createTempFile("MogwaiChart", ".gif");
            String args[] = new String[4];
            args[0] = this.m_dotexe;
            args[1] = "-o" + tempfile2;
            args[2] = "-Tgif";
            args[3] = tempfile.toString();
            this.m_logger.info("Invoking dot");
            final Process p = Runtime.getRuntime().exec(args, null, new File(tempfile.getParent()));
            Thread runner = new Thread() {

                public void run() {
                    try {
                        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
                        String line = null;
                        while ((line = br.readLine()) != null) {
                            m_logger.info("Out->" + line);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            runner.start();
            Thread runner2 = new Thread() {

                public void run() {
                    try {
                        BufferedReader br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                        String line = null;
                        while ((line = br.readLine()) != null) {
                            m_logger.info("Error->" + line);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            runner2.start();
            p.waitFor();
            while (runner.isAlive()) ;
            while (runner2.isAlive()) ;
            this.m_logger.info("Loading graph...");
            ImageIcon icon = new ImageIcon(tempfile2.toString());
            JLabel displayLabel = new JLabel(icon);
            displayLabel.setTransferHandler(new ImageSelection());
            this.m_logger.info("Copying graph to clipboard...");
            Clipboard board = Toolkit.getDefaultToolkit().getSystemClipboard();
            displayLabel.getTransferHandler().exportToClipboard(displayLabel, board, TransferHandler.COPY);
            this.m_resultPanel.removeAll();
            this.m_resultPanel.add(displayLabel);
            tempfile.delete();
            tempfile2.delete();
            this.m_logger.info("Done...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
