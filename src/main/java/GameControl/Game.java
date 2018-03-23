package GameControl;

import java.util.List;

import Enums.MouseAction;
import Enums.CellStatus;


public class Game
{

    private Board board;
    private boolean hasEnded;
    private int score;
    //TODO score saver --> score is a list


    public Board getBoard()
    {
        return board;
    }

    public boolean isHasEnded()
    {
        return hasEnded;
    }

    public int getScore()
    {
        return score;
    }

    public void setScore(int score)
    {
        this.score = score;
    }



    public Game(Board board)
    {
        this.board = board;
        score = 0;
    }




    //main method called for each play
    public void executePlay(int cellId, MouseAction mouseAction)
    {
        List<Cell> cells = board.getCells();
        Cell cell = cells.get(cellId);


        if (mouseAction == MouseAction.LEFT_CLICK)
        {
            if (cell.getStatus() == CellStatus.CLOSED)
            {
                if (cell.isMined())
                    loseGame();
                else
                    //this method will open all necessary cells
                    openCell(cell);

            }

            //left click on open cells do nothing
        }
        else
        {

            if (cell.getStatus() == CellStatus.CLOSED)
                cell.setStatus(CellStatus.FLAGGED);
            //take of the flag
            else if (cell.getStatus() == CellStatus.FLAGGED)
                cell.setStatus(CellStatus.CLOSED);

            //right clicks on open cells do nothing
        }

        // check after each play if the game is won
        if (isGameWon())
            winGame();
    }


    //open multiple empty cells
    private void openCell(Cell cell)
    {
        cell.reveal();

        //if cell has no mined around, open all empty cells
        if (cell.getMinedNeighbors()==0)
            openNeighbors(cell);
    }

    private void openNeighbors(Cell cell)
    {
        //check neighbors

        int length = board.getBoardSize().getLength();
        int height = board.getBoardSize().getHeight();

        int cellX = cell.getId() % length;
        int cellY = cell.getId() / height;

        //check all neighbors
        //TODO make a cell method --> getNeighbors
        for (int y = cellY - 1; y <= cellY + 1; y++)
        {
            for (int x = cellX - 1; x <= cellX + 1; x++)
            {
                if (y != -1 && x != -1 && y != height && x != length)
                {
                    Cell neighbor = board.getCells().get(y * length + x);

                    //neighbours cannot be mined because cell doesn't have any mines around
                    //if neighbors is empty and not already opened open it's neighbours
                    if (neighbor.getMinedNeighbors() == 0 && neighbor.getStatus()!=CellStatus.OPENED)
                        openCell(neighbor);


                    neighbor.reveal();
                }
            }
        }
    }

    private void loseGame()
    {
        //open all mined cells in order to show them
        for (Cell cell : board.getCells())
        {
            if (cell.isMined())
                cell.setStatus(CellStatus.OPENED);
        }

        endGame();
    }

    public boolean isGameWon()
    {
        boolean gameWon = true;
        int numCell = 0;

        while(gameWon && numCell<board.getCells().size())
        {
            Cell cell = board.getCells().get(numCell);
            //if a not mined cell is not opened, the game is not won
            if (!cell.isMined() && cell.getStatus()!=CellStatus.OPENED)
                gameWon=false;
            numCell++;
        }

        return (gameWon);
    }


    private void winGame()
    {
            endGame();
    }


    private void endGame()
    {
        this.hasEnded = true;
    }


    //restarting a game keeps the same board
    public void restartGame()
    {
        this.hasEnded=false;
        //setting all cells to closed resets the game
        for(Cell cell : board.getCells())
            cell.closeCell();

        score=0;
    }

    //main method to check the board in command line in command line

/*
     void commandLineControl()
    {
        Board board = new Board(BoardSize.BEGINNER);
        Game game = new Game(board);
        game.board.print();

        int userInput;
        Scanner reader = new Scanner(System.in);
        while(!game.hasEnded)
        {
            System.out.println("Enter a number: ");
            userInput = reader.nextInt();
            game.executePlay(userInput,Action.LEFT_CLICK);
            game.board.print();
        }
        reader.close();
    }



    public static void main(String[] args)
    {
        commandLineControl();
    }
*/

}
