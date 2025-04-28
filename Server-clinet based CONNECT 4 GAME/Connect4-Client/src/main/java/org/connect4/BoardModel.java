package org.connect4;

import java.io.IOException;

/**
 * BoardModel manages game state and network messaging for Connect4.
 * - 6 rows x 7 columns: 0=empty, 1=you, 2=opponent
 * - Sends your moves when you play (player == 1)
 * - Simply records opponent moves when received (player == 2)
 */
public class BoardModel {
    public static final int ROWS = 6;
    public static final int COLS = 7;
    private final int[][] board;
    public static String opponent;

    public BoardModel() {
        board = new int[ROWS][COLS];
        resetBoard();
    }

    public void setOpponent(String opponent) {
        this.opponent = opponent;
    }

    /**
     * Drop a disc into the column for the given player.
     * @param col 0-based column index
     * @param player 1 = you, 2 = opponent
     * @return the row index where the disc landed, or -1 if the column was full
     */
    public int makeMove(int col, int player) {
        int row = findRow(col);
        if (row < 0) {
            return -1;  // column full
        }
        board[row][col] = player;

        if (player == 1) {
            sendMove(col);
        }else {
            Client.gameboard.handleOpponentMove(col,row);
        }
        return row;
    }

    /**
     * Send a MOVE message to the opponent over Client.out.
     */
    private void sendMove(int col) {
        if (opponent == null) {
            System.err.println("Opponent not set – cannot send move.");
            return;
        }
        try {
            Communication msg = new Communication(
                    Client.username,
                    opponent,
                    Communication.MOVE,
                    Integer.toString(col)
            );
            Client.out.writeObject(msg);
        } catch (IOException e) {
            System.err.println("Failed to send move: " + e.getMessage());
        }
    }

    /**
     * Find the lowest empty row in the column, or -1 if the column is full.
     */
    public int findRow(int col) {
        for (int r = ROWS - 1; r >= 0; r--) {
            if (board[r][col] == 0) {
                return r;
            }
        }
        return -1;
    }

    /**
     * Check if a column is full.
     */
    public boolean isFull(int col) {
        return board[0][col] != 0;
    }

    /**
     * Reset the board to all empty cells.
     */
    public void resetBoard() {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                board[r][c] = 0;
            }
        }
    }

    /**
     * Get the internal board array (for UI or win-checking).
     */
    public int[][] getBoard() {
        return board;
    }
}
