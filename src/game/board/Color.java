package game.board;

public enum Color {
    Black ("\u001B[30m"),
    White ("\u001B[97m");

    public final String code;

    Color(String code){
        this.code = code;
    }
}
