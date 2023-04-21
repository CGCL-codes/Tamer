    public void receiveEvent(Event e) {
        if (e.getTipus() == Event.PAQUET_ENTRA_SISTEMA) {
            entrades++;
        }
        if (e.getTipus() == Event.PAQUET_DESCARTAT) {
            descartats.add(e);
            num_descartats[((Packet) e.getSource()).getTipus()]++;
        }
        if (e.getTipus() == Event.TEMPS_PROCESSAMENT_ENTRADA) {
            long t_proc = e.getTime() - ((Event) e.getSource()).getTime();
            if (t_proc_mig == 0) t_proc_mig = t_proc;
            t_proc_mig = (t_proc_mig + t_proc) / 2;
        }
    }
