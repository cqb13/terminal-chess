package game;

import game.board.Color;

public enum Player {
    One (Color.White),
    Two (Color.Black);

    public final Color color;

    Player(Color color){
        this.color = color;
    }
}
