        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                this.setText(memories.get(read_index));
                read_index--;
                if (read_index < 0) {
                    read_index = (memories.size() - 1);
                }
            } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                this.setText(memories.get(read_index));
                read_index++;
                read_index = read_index % memories.size();
            } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                memories.add(write_index, this.getText());
                read_index = write_index;
                write_index++;
                write_index = write_index % MEMORY_SIZE;
            }
        }
