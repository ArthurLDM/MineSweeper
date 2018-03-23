package GameControl;


import Enums.CellStatus;

public class Cell
{

    private int id;
    private CellStatus status;
    private boolean mined;
    private int minedNeighbors;

    public int getId()
    {
        return id;
    }

    public int getMinedNeighbors()
    {
        return minedNeighbors;
    }

    public void setMinedNeighbors(int minedNeighbours)
    {
        this.minedNeighbors = minedNeighbours;
    }



    private Cell(int id)
    {
        this.id = id;

        this.status = CellStatus.CLOSED;
        this.mined = false;
        this.minedNeighbors=0;
    }

    public boolean isMined()
    {
        return mined;
    }

    public void setMine()
    {
        this.mined = true;
    }

    public CellStatus getStatus()
    {
        return status;
    }

    public void setStatus(CellStatus status)
    {
        //if a cell is opened, it cannot be closed or flagged
        //if a cell is flagged it cannot be opened
        if (this.status != CellStatus.OPENED)
            if(!(this.status== CellStatus.FLAGGED && status== CellStatus.OPENED))
                this.status=status;
    }


    public void reveal()
    {
        if(status!=CellStatus.FLAGGED)
            setStatus(CellStatus.OPENED);
    }
    public void closeCell() {status= CellStatus.CLOSED;}

}
