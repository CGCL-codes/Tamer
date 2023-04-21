package com.thoughtworks.twu.gameoflife.domain;

import com.thoughtworks.twu.gameoflife.GameOfLife;

public interface GameListener {

    GameListener NULL = new GameListener() {

        public void cellsChanged(GameOfLife cells) {
        }
    };

    void cellsChanged(GameOfLife cells);
}
