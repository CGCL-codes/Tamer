    private void addMouseListener() {
        fGraphicalViewer.getControl().addMouseListener(new MouseAdapter() {

            @Override
            public void mouseDown(MouseEvent e) {
                setMouseClickPosition(e.x, e.y);
            }
        });
    }
