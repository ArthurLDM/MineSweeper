package GameControl;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Enums.BoardSize;
import Enums.CellStatus;

public class Board
{

    private List<Cell> cells;
    private BoardSize boardSize;


    public List<Cell> getCells()
    {
        return cells;
    }

    public BoardSize getBoardSize()
    {
        return boardSize;
    }


    public Board(BoardSize boardSize)
    {

        this.boardSize = boardSize;

        //creating cells
        cells = new ArrayList<>();
        int numOfCell = 0;
        for (int y = 0; y < boardSize.getHeight(); y++)
        {
            for (int x = 0; x<boardSize.getLength(); x++)
            {
                //the cell's ID is it's number
                Cell cell = new Cell(numOfCell);
                cells.add(cell);
                numOfCell+=1;
            }
        }

        //create mines
        List<Integer> mines = createMines(numOfCell,boardSize.getNumMines());
        //set mines
        mineCells(mines);
        //count the mined neighbors
        initNumNeighbors(mines);

    }



    private List<Integer> createMines(int numOfCells, int numOfMines)
    {

        Random rand = new Random();
        List<Integer> minePositions = new ArrayList<>();

        int index;
        while(minePositions.size()<numOfMines)
        {
            index = rand.nextInt(numOfCells);
            if (!minePositions.contains(index))
                // a mine is a cell index
                minePositions.add(index);
        }
        return(minePositions);
    }

    private void mineCells(List<Integer> mines)
    {
        for (int mine : mines)
        {
            Cell cell = cells.get(mine);
            cell.setMine();
            cells.set(mines.get(0),cell);
        }
    }


    private void initNumNeighbors(List<Integer> mines)
    {
        int length = boardSize.getLength();
        int height = boardSize.getHeight();

        //for each mine, add +1 to all surrounding cells
        for (int mine : mines)
        {
            int mineX = mine % length;
            int mineY = mine / height;

            //finding all surrounding cells
            for (int y = mineY - 1; y <= mineY + 1; y++)
            {
                for (int x = mineX - 1; x <= mineX + 1; x++)
                {
                    //make sure the cell is not oute of the board
                    if(y!=-1 && x!=-1 && y!=height && x!=length)
                    {
                        Cell cell = cells.get(y * length + x);
                        if (!cell.isMined())
                            cell.setMinedNeighbors(cell.getMinedNeighbors() + 1);
                    }
                }
            }
        }
    }

    //displays the board for command line execution
    //displays each cell state in the following format
    // | open/closed + flagged/mined/number of mines around|

    /////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////
    ///THIS METHOD IS NOT FOR PLAYING BUT FOR VERIFICATION///
    public void print()
    {
        System.out.println("\n");
        int length = boardSize.getLength();
        int height = boardSize.getHeight();

        int cell = 0;
        //printing row by row
        for (int x = 0 ; x < length; x++)
        {
            StringBuilder row = new StringBuilder("|");
            for (int y = 0; y < height; y++)
            {
                //display open/closed
                if (cells.get(cell).getStatus()== CellStatus.OPENED)
                    row.append("o");
                else
                    row.append("c");


                // display mined/flagged/number of mines around
                if (cells.get(cell).isMined())
                    row.append("M" + "|");

                else if (cells.get(0).getStatus()== CellStatus.FLAGGED)
                    row.append("F" + "|");

                else
                    row.append(String.valueOf(cells.get(cell).getMinedNeighbors())).append("|");


                cell += 1;
            }
            System.out.println(row);
        }
        System.out.println("\n");
    }
}
