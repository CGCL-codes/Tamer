package org.plide;

import java.awt.*;

class LeftFrame extends PlideInternalFrame {

    LeftFrame(String title, PlideFrame main, TabBar tBar) {
        super(title, main);
        init();
        mTBar = tBar;
        add(mTBar, BorderLayout.NORTH);
    }

    private void init() {
        setResizable(true);
        setClosable(true);
        setVisible(true);
        setLayout(new BorderLayout());
        setSize(200, 400);
    }
}
