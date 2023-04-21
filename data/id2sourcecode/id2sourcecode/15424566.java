    @Override
    public String execute(final String... params) throws Exception {
        LOGGER.debug("======================== Running BLAST...");
        if (params[0].equals("EXECUTE")) {
            f = executor.submit(new Callable<String>() {

                @Override
                public String call() throws Exception {
                    try {
                        String remoteDir = params[1];
                        String filename = params[2];
                        String host = params[3];
                        LOGGER.debug("BlastServer.execute() = RECUPERANDO ARQUIVO FTP *************** !");
                        File file = SftpClient.getFile(remoteDir, appConfig.getOutputDirectory(), filename, host, 22);
                        LOGGER.debug("BlastServer.execute() = ARQUIVO RECUPERADO *************** !");
                        LOGGER.debug("************************************* File retrieved!!!! ********************************* ");
                        LOGGER.debug("***************** executing task " + file.getAbsolutePath() + " " + file.getAbsolutePath() + ".out ***********");
                        LOGGER.debug("***************** comando " + appConfig.getPath() + " " + file.getAbsolutePath() + " " + file.getAbsolutePath() + ".out");
                        contigInExecution = file.getName();
                        p = Runtime.getRuntime().exec(appConfig.getPath() + "  " + file.getAbsolutePath() + " " + file.getAbsolutePath() + ".out");
                        InputStream is = p.getInputStream();
                        BufferedReader br = new BufferedReader(new InputStreamReader(is));
                        String line = null;
                        StringBuilder sb = new StringBuilder();
                        while ((line = br.readLine()) != null) {
                            sb.append(line);
                        }
                        LOGGER.debug("***************************************** Resultado: " + sb.toString() + "**************************************");
                        BufferedReader error = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                        String line2 = null;
                        StringBuilder sb2 = new StringBuilder();
                        while ((line2 = br.readLine()) != null) {
                            sb.append(line2);
                        }
                        LOGGER.error("***************************************** na execu��o do COMANDO blast: " + sb.toString());
                        return sb.toString();
                    } catch (Exception e) {
                        LOGGER.error("***************************** " + e.getMessage());
                        throw new RuntimeException(e);
                    }
                }
            });
        } else if (params[0].equals("EXECUTE-TORRENT")) {
            f = executor.submit(new Callable<String>() {

                @Override
                public String call() throws Exception {
                    try {
                        String remoteDir = params[1];
                        String filename = params[2];
                        String host = params[3];
                        File file = null;
                        if (!params[4].equalsIgnoreCase("LOCAL")) {
                            String name_file = remoteDir + filename;
                            LOGGER.debug("BlastServer.execute() = RECUPERANDO ARQUIVO " + name_file + " para FTP - ARQUIVO TORRENT!");
                            file = SftpClient.getFile(remoteDir, appConfig.getOutputDirectory(), filename, host, 22);
                            LOGGER.debug("BlastServer.execute() = ARQUIVO torrent torrent torrent RECUPERADO *************** !");
                        } else {
                            LOGGER.debug("BlastServer.execute() = DOWNLOAD DO ARQUIVO TORRENT PARA EXECU��O!");
                            file = copyFile(new File(remoteDir + filename), new File(File.separator + "tmp" + File.separator + filename));
                        }
                        LOGGER.debug("BlastServer.execute() - DOWNLOAD DOWNLOAD DOWNLOAD DOWNLOAD DOWNLOAD");
                        String[] data = { file.getAbsolutePath() };
                        LOGGER.debug("TorrentManager.executeOption() - Download do arquivo torrent:" + data[0]);
                        DownloaderOfFiles dof = new DownloaderOfFiles(data, LOGGER);
                        if (dof.executeDonwload()) LOGGER.debug("BlastServer.execute() = File downloaded!"); else LOGGER.debug("BlastServer.execute() = ERROR in download torrent process!");
                    } catch (Exception ex) {
                        LOGGER.debug("BlastServer.execute() = Erro na recupera��o do ARQUIVO = " + ex.getMessage());
                    }
                    return "";
                }
            });
        } else if (params[0].equals("SFTP-DOWNLOAD")) {
            f = executor.submit(new Callable<String>() {

                @Override
                public String call() throws Exception {
                    try {
                        String remoteDir = params[1];
                        String filename = params[2];
                        String host = params[3];
                        long inicio = System.currentTimeMillis();
                        LOGGER.debug("BlastServer.execute() = RECUPERANDO ARQUIVO FTP *************** !");
                        File file = SftpClient.getFile(remoteDir, appConfig.getOutputDirectory(), filename, host, 22);
                        long fim = System.currentTimeMillis();
                        long total = fim - inicio;
                        LOGGER.debug("BlastServer.execute() = ARQUIVO RECUPERADO em " + total / 1000 + " segundos!!!!");
                        LOGGER.debug("************************************* File retrieved!!!! ********************************* ");
                    } catch (Exception ex) {
                        LOGGER.debug("BlastServer.execute() = Erro na recupera��o do ARQUIVO = " + ex.getMessage());
                    }
                    return "";
                }
            });
        } else if (params[0].equals("STATUS")) {
            if (f == null) {
                return "400 NOTHING HAPPENS YET";
            }
            LOGGER.debug("job is done : " + f.isDone() + " contig: " + contigInExecution);
            if (f.isCancelled()) return "300: JOB CANCELLED contig: " + contigInExecution;
            if (f.isDone()) return "300: JOB FINISHED : " + f.get() + " contig : " + contigInExecution;
            return "200: JOB RUNNING";
        } else if (params[0].equals("CANCEL")) {
            LOGGER.debug("******************************************************* Mensagem de cancelamento recebida !!!!!");
            Process proc = Runtime.getRuntime().exec("pgrep -u biofoco blastall");
            int read = 0;
            byte[] buf = new byte[1024];
            InputStream is = proc.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ((read = is.read(buf, 0, buf.length)) != -1) {
                baos.write(buf, 0, read);
            }
            LOGGER.debug("::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: " + baos.toString());
            if (baos.toString() != null && baos.toString().trim().length() > 0) {
                StringTokenizer st = new StringTokenizer(baos.toString().trim(), " ");
                String pid = st.nextToken();
                Runtime.getRuntime().exec("kill -9 " + pid);
                LOGGER.debug("************************************************************* CANCELANDO JOB");
                boolean resp = f.cancel(true);
                f.isCancelled();
                return "300: JOB CANCELLED " + resp + " contig: " + contigInExecution;
            }
            return "300: JOB CANCELLED ";
        }
        return "200:JOB SUBMITTED";
    }
