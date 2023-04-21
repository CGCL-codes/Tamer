        void undeploy(boolean deleteDeployFile) throws IOException {
            HashSet<URL> closedJarURLs = new HashSet<URL>();
            for (URL url : getURLs()) {
                if ("jar".equals(url.getProtocol())) {
                    try {
                        URLConnection connection = url.openConnection();
                        if (connection instanceof JarURLConnection) {
                            JarURLConnection jarConnection = (JarURLConnection) connection;
                            if (closedJarURLs.add(jarConnection.getJarFileURL())) {
                                JarFile jarFile = jarConnection.getJarFile();
                                if (jarFile != null) jarFile.close();
                            }
                        }
                    } catch (final Exception ex) {
                        DeploymentDirectoryMonitor.this.getLogger().log(Level.SEVERE, "Exception while closing deployment file:" + "\n  Deployment file = " + this.deployFile + "\n  Work       file = " + this.workFile, ex);
                    }
                }
            }
            boolean deletedWorkFile = Files.delete(this.workFile);
            boolean deletedDeployFile = !deleteDeployFile || (deletedWorkFile && Files.delete(this.deployFile));
            if (!deletedWorkFile) throw new IOException("Unable to delete work file: " + this.workFile);
            if (!deletedDeployFile) throw new IOException("Unable to delete deployment file: " + this.deployFile);
        }
