    public int read() throws IOException {
        int read = in.read();
        if (read == -1) return -1;
        switch(state) {
            case (LINEEND):
                {
                    line_length = 0;
                    state = IN_LINE;
                    logOutputStream.write(PREFIX_STRING);
                    break;
                }
            case (IN_LINE):
                {
                    line_length++;
                    if (read == '\n') {
                        state = LINEEND;
                    } else if (line_length == MAX_LENGTH) {
                        line_length = 0;
                        logOutputStream.write('\\');
                        logOutputStream.write('\n');
                        logOutputStream.write(PREFIX_STRING);
                    }
                    break;
                }
        }
        logOutputStream.write(read);
        return read;
    }
