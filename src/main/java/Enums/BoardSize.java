package Enums;

public enum BoardSize
{
    BEGINNER(9,9,10), INTERMEDIATE(16,16,40), EXPERT(24,24,99), CUSTOMIZED(0,0,0);


    private int length;
    private int height;
    private int numMines;

    BoardSize(int length, int height, int numMines)
    {
        this.length = length;
        this.height = height;
        this.numMines=numMines;
    }

    public int getHeight()
    {
        return height;
    }

    public int getLength()
    {
        return length;
    }

    public int getNumMines()
    {
        return numMines;
    }

    //customized board can be modified
    public void setLength(int length)
    {
        if (this == CUSTOMIZED)
            this.length = length;
    }

    public void setHeight(int height)
    {
        if (this == CUSTOMIZED)
            this.height = height;
    }

    public void setNumMines(int numMines)
    {
        if (this == CUSTOMIZED)
            this.numMines = numMines;
    }
}