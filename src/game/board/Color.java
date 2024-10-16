package game.board;
/*

        final String WHITE_FG = "\u001B[97m";
        final String BLACK_FG = "\u001B[30m";
 */
public enum Color {
    Black ("\u001B[30m"),
    White ("\u001B[97m");

    public final String code;

    Color(String code){
        this.code = code;
    }
}
