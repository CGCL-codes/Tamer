            @Override
            protected IStatus run(final IProgressMonitor monitor) {
                tileInfoMgr.updateSRTMTileInfo(TileEventId.SRTM_DATA_START_LOADING, remoteFileName, 0);
                final String address = urlBase + remoteFileName;
                System.out.println("load " + address);
                OutputStream outputStream = null;
                InputStream inputStream = null;
                try {
                    final URL url = new URL(address);
                    outputStream = new BufferedOutputStream(new FileOutputStream(localFilePathName));
                    final URLConnection urlConnection = url.openConnection();
                    inputStream = urlConnection.getInputStream();
                    final byte[] buffer = new byte[1024];
                    int numRead;
                    while ((numRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, numRead);
                        numWritten[0] += numRead;
                    }
                    System.out.println("# Bytes localName = " + numWritten);
                } catch (final UnknownHostException e) {
                    return new Status(IStatus.ERROR, TourbookPlugin.PLUGIN_ID, IStatus.ERROR, NLS.bind(Messages.error_message_cannotConnectToServer, urlBase), e);
                } catch (final SocketTimeoutException e) {
                    return new Status(IStatus.ERROR, TourbookPlugin.PLUGIN_ID, IStatus.ERROR, NLS.bind(Messages.error_message_timeoutWhenConnectingToServer, urlBase), e);
                } catch (final Exception e) {
                    return new Status(IStatus.ERROR, TourbookPlugin.PLUGIN_ID, IStatus.ERROR, e.getMessage(), e);
                } finally {
                    try {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        if (outputStream != null) {
                            outputStream.close();
                        }
                    } catch (final IOException e) {
                        e.printStackTrace();
                    }
                    tileInfoMgr.updateSRTMTileInfo(TileEventId.SRTM_DATA_END_LOADING, remoteFileName, 0);
                }
                System.out.println("get " + remoteFileName + " -> " + localFilePathName + " ...");
                return Status.OK_STATUS;
            }
