                    @Override
                    public void write(int b) throws IOException {
                        sb.append((char) b);
                        for (int i = 0; i < buff.length - 1; i++) {
                            buff[i] = buff[i + 1];
                        }
                        buff[buff.length - 1] = (char) b;
                        String tmp = String.valueOf(buff);
                        if (tmp.equals(endLineMark)) {
                            textPane.setText(sb.toString());
                        }
                    }
