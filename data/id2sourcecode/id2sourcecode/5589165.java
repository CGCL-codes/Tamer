    private Reply doSend(ConsignJob request, PortfolioTh[] portfolios) throws Connection.Exception {
        if (portfolios == null) return doSend(request);
        try {
            if (output == null) {
                output = new ObjectOutputStream(socket.getOutputStream());
            }
            output.reset();
            output.writeObject(request);
            output.flush();
            ZipOutputStream zos = getDataOutputStream();
            for (int i = 0; i < portfolios.length; i++) {
                String dir_name = portfolios[i].getPortfolio().getUPLDirectoryName() + "/";
                for (int j = 0; j < portfolios[i].getFiles().length; j++) {
                    if (portfolios[i].getFiles()[j].exists()) {
                        String name;
                        if (portfolios[i].getDestinationNames() == null) {
                            name = portfolios[i].getFiles()[j].getName();
                        } else {
                            name = portfolios[i].getDestinationNames()[j];
                        }
                        transferFiles(dir_name, portfolios[i].getFiles()[j], name, zos);
                    } else {
                        CLogger.status("Adding no overwrite marker to stream <" + dir_name + "> " + portfolios[i].getFiles()[j].getName());
                        ZipEntry marker = new ZipEntry(dir_name);
                        marker.setExtra(new byte[1]);
                        zos.putNextEntry(marker);
                        zos.closeEntry();
                    }
                }
            }
            CLogger.status("Done sending streamed");
            doneWithDataOutputStream();
            return (Reply) getObjectInputStream().readObject();
        } catch (java.lang.Exception ex) {
            _close();
            throw new Connection.Exception("Error sending Request with streamed: " + ex.getMessage());
        }
    }
