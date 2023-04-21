        private String download(PluginManagerProgress progress, String fileName, String url) {
            try {
                URLConnection conn = new URL(url).openConnection();
                String path = MiscUtilities.constructPath(getDownloadDir(), fileName);
                if (!copy(progress, conn.getInputStream(), new FileOutputStream(path), true)) return null;
                return path;
            } catch (InterruptedIOException iio) {
                return null;
            } catch (final IOException io) {
                Log.log(Log.ERROR, this, io);
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        String[] args = { io.getMessage() };
                        GUIUtilities.error(null, "ioerror", args);
                    }
                });
                return null;
            } catch (Exception e) {
                Log.log(Log.ERROR, this, e);
                return null;
            }
        }
