package org.connect4;

public class GameSession {
    private int[][] board;
    public ClientHandler player1;
    public ClientHandler player2;
    private int rows = 6;
    private int cols = 7;

    public GameSession(ClientHandler player1, ClientHandler player2) {
        this.board = new int[rows][cols]; // 0 = empty, 1 = player1, 2 = player2
        this.player1 = player1;
        this.player2 = player2;

        //player1.setGameSession(this);
        player2.setGameSession(this);
    }

    public boolean isWinningMove(int player) {
        // Horizontal
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col <= cols - 4; col++) {
                if (board[row][col] == player &&
                        board[row][col+1] == player &&
                        board[row][col+2] == player &&
                        board[row][col+3] == player) return true;
            }
        }
        // Vertical
        for (int col = 0; col < cols; col++) {
            for (int row = 0; row <= rows - 4; row++) {
                if (board[row][col] == player &&
                        board[row+1][col] == player &&
                        board[row+2][col] == player &&
                        board[row+3][col] == player) return true;
            }
        }
        // Diagonal (\)
        for (int row = 0; row <= rows - 4; row++) {
            for (int col = 0; col <= cols - 4; col++) {
                if (board[row][col] == player &&
                        board[row+1][col+1] == player &&
                        board[row+2][col+2] == player &&
                        board[row+3][col+3] == player) return true;
            }
        }
        // Diagonal (/)
        for (int row = 3; row < rows; row++) {
            for (int col = 0; col <= cols - 4; col++) {
                if (board[row][col] == player &&
                        board[row-1][col+1] == player &&
                        board[row-2][col+2] == player &&
                        board[row-3][col+3] == player) return true;
            }
        }

        return false;
    }

    public boolean isFull() {
        for (int col = 0; col < cols; col++) {
            if (board[0][col] == 0) return false;
        }
        return true;
    }

    public void move(ClientHandler player, int col) {
        int playerNumber = (player == player1) ? 1 : 2;

        for (int row = rows - 1; row >= 0; row--) {
            if (board[row][col] == 0) {
                board[row][col] = playerNumber;
                notifyPlayersMove(player, col);
                if (isWinningMove(1)){
                    System.out.println("prayer "+player1.getPlayerName()+ " won");

                    Communication endMsg = new Communication("server", player1.getPlayerName(), Communication.END_GAME, "win");
                    if (player1 != null ) {
                        player1.message(endMsg);
                        player1.setGameSession(null);
                    }
                    if (player2 != null ) {
                        player2.message(new Communication("server", player2.getPlayerName(), Communication.END_GAME, "lose"));
                        player2.setGameSession(null);
                    }

                }else if (isWinningMove(2)){
                    System.out.println("prayer "+player2.getPlayerName()+ " won");
                    Communication endMsg = new Communication("server", player2.getPlayerName(), Communication.END_GAME, "win");
                    if (player2 != null ) {
                        player2.message(endMsg);
                        player2.setGameSession(null);
                        if (player1 != null ) {
                            player1.setGameSession(null);
                        }

                        if (player2 != null ) {
                            player2.setGameSession(null);
                        }
                    }
                    if (player1 != null ) {
                        player1.message(new Communication("server", player1.getPlayerName(), Communication.END_GAME, "lose"));
                        player1.setGameSession(null);
                        if (player1 != null ) {
                            player1.setGameSession(null);
                        }

                        if (player2 != null ) {
                            player2.setGameSession(null);
                        }
                    }
                }else if (isFull()){
                    endGame("TIE");
                }
                return;
            }
        }
    }

    private void notifyPlayersMove(ClientHandler mover,  int col) {
        String moveMsg = Integer.toString(col);
        // opponent notified
        if (mover == player1) {
            player2.message(new Communication("server", player2.getPlayerName(), Communication.MOVE, moveMsg));
        } else {
            player1.message(new Communication("server", player1.getPlayerName(), Communication.MOVE, moveMsg));
        }
    }

    public void endGame(String reason) {
        Communication endMsg = new Communication("server", "players", Communication.END_GAME,  reason);

        if (player1 != null ) {
            player1.setGameSession(null);
        }

        if (player2 != null ) {
            player2.setGameSession(null);
        }
    }

    public void resetBoard() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                board[i][j] = 0;
            }
        }
    }

}
