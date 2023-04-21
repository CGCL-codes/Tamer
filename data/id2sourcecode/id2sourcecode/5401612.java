            public void write(byte[] bbuf, int offset, int bytes) throws IOException {
                long newIndex = _writeIndex + bytes;
                while (newIndex > _readIndex) {
                    if (_eof) {
                        _readIndex = newIndex;
                    } else {
                        int bufferWriteResult = toBuffer.write(in, (int) (newIndex - _readIndex));
                        if (bufferWriteResult < 0) {
                            _eof = true;
                        } else {
                            _readIndex += bufferWriteResult;
                        }
                    }
                }
                out.write(bbuf, offset, bytes);
                _writeIndex = newIndex;
            }
