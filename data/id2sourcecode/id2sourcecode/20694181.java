    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource().equals(btn_Go)) {
            new Thread(this, "Randomizer_load").start();
        } else if (evt.getSource().equals(btn_Randomize)) {
            new Thread(this, "Randomizer_write").start();
        }
    }
