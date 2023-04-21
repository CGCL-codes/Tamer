        public void actionPerformed(ActionEvent _e) {
            DjaVector vect = grid_.getSelection();
            int size = vect.size();
            if (size > 1) {
                int max = 0;
                int min = grid_.getWidth();
                for (int i = 0; i < size; i++) {
                    DjaForm djaForm = ((DjaForm) vect.elementAt(i));
                    max = Math.max(max, djaForm.getX() + djaForm.getWidth());
                    min = Math.min(min, djaForm.getX());
                }
                int center = (min + max) / 2;
                for (int i = 0; i < size; i++) {
                    DjaForm djaForm = ((DjaForm) vect.elementAt(i));
                    djaForm.setX(center - djaForm.getWidth() / 2);
                }
            }
            fireModified();
        }
