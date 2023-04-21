            public void write(char[] cbuf, int offset, int chars) throws IOException {
                long newIndex = _writeIndex + chars;
                while (newIndex > _readIndex) {
                    if (_eof) {
                        _readIndex = newIndex;
                    } else {
                        int bufferWriteResult = toBuffer.write(r, (int) (newIndex - _readIndex));
                        if (bufferWriteResult < 0) {
                            _eof = true;
                        } else {
                            _readIndex += bufferWriteResult;
                        }
                    }
                }
                w.write(cbuf, offset, chars);
                _writeIndex = newIndex;
            }
