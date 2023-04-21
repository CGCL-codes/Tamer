        public void actionPerformed(ActionEvent _e) {
            DjaVector vect = grid_.getSelection();
            int size = vect.size();
            if (size > 1) {
                int max = 0;
                int min = grid_.getHeight();
                for (int i = 0; i < size; i++) {
                    DjaForm djaForm = (DjaForm) vect.elementAt(i);
                    max = Math.max(max, djaForm.getY() + djaForm.getHeight());
                    min = Math.min(min, ((DjaForm) vect.elementAt(i)).getY());
                }
                int middle = (max + min) / 2;
                for (int i = 0; i < size; i++) {
                    DjaForm djaForm = ((DjaForm) vect.elementAt(i));
                    djaForm.setY(middle - djaForm.getHeight() / 2);
                }
            }
            fireModified();
        }
