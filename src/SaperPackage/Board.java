package SaperPackage;

public abstract class Board {
    private int[][] board;

    public Board(){
        this.board = null;
    }

    public abstract void fillGameBoard();

    public int[][] getBoard(){
        return board;
    }

    public void setBoard(int[][] board){
        this.board = board;
    }
}
