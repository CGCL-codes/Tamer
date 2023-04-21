            public void run() {
                synchronized (_lock) {
                    checkClosed();
                    if (_backupFile != null) {
                        throw new BackupInProgressException();
                    }
                    _backupFile = new BlockAwareBin(targetStorage.open(new BinConfiguration(path, true, _file.length(), false, _blockConverter.blocksToBytes(1))));
                }
                long pos = 0;
                byte[] buffer = new byte[8192];
                while (true) {
                    synchronized (_lock) {
                        int read = _file.read(pos, buffer);
                        if (read <= 0) {
                            break;
                        }
                        _backupFile.write(pos, buffer, read);
                        pos += read;
                    }
                    Runtime4.sleep(1);
                }
                synchronized (_lock) {
                    try {
                        syncAndClose(_backupFile);
                    } finally {
                        _backupFile = null;
                    }
                }
            }
