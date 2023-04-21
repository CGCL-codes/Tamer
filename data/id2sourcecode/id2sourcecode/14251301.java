        public void showPopup(MouseEvent e) {
            int trk = rowAtPoint(e.getPoint());
            MidiTrack track = ((TrackTableModel) getModel()).getSequence().getMidiTrack(trk);
            if (track.getChannel() < 0) {
                return;
            }
            changeSelection(trk, -1, false, false);
            ((TrackPopupMenu) popup).setTrack(track);
            super.showPopup(e);
        }
