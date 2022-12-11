package com.example.battleship;

public class Board {

    public Field[][] boardGame;

    public Board() {
        boardGame = new Field[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                boardGame[i][j] = new Field(i, j, Constant.WATER);
            }
        }
    }
    public Field getFieldById(String id) {
        int x = Integer.parseInt(id.substring(1))-1;
        int y = (int)id.charAt(0) - (int)'A';
        return new Field(x, y);
    }

    public void displayBoard() {
        for (int i = 0; i < 10; i ++) {
            for (int j = 0; j < 10; j++) {
                System.out.print(boardGame[i][j].getState() + " ");
            }
            System.out.println();
        }
    }
    public boolean possiblePutShip(Field f, char dir, int size) {
        int x = f.getX();
        int y = f.getY();

        for (int i=0; i<size; i++)
        {
            if ((dir == Constant.HORIZONTAL && (y+i)>=Constant.BOARD_SIZE) ||
                    (dir == Constant.VERTICAL && (x+i)>=Constant.BOARD_SIZE))
                return false;
            if ((dir == Constant.HORIZONTAL && boardGame[x][y+i].getState() != Constant.WATER) ||
                    (dir == Constant.VERTICAL && boardGame[x+i][y].getState() != Constant.WATER))
                return false;

        }
        return true;
    }

    public String makeBoardAsMessageToServer() {
        StringBuilder message = new StringBuilder();

        for (int i =0 ; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                message.append(boardGame[i][j].getState());
            }
        }

        return message.toString();
    }


}
