import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    final private WeightedQuickUnionUF quickUnionSystem;
    final private WeightedQuickUnionUF backwash;
    private boolean[] matrixArray;
    final private int topSquare = 0;
    final private int bottomSquare;
    final private int size;
    private int openSites;


    //creates n-by-n grid, with all sites initially blocked
    public Percolation(int n)
    {
        if (n <= 0)
            throw new IllegalArgumentException("Can't work with thoose parameters, must be greeater than 1");
        backwash = new WeightedQuickUnionUF(n * n + 2);
        quickUnionSystem = new WeightedQuickUnionUF(n * n + 2);
        matrixArray = new boolean[n * n + 1];
        bottomSquare = n * n + 1;
        size = n;
        //System.out.println("We created a matrix of " + n + " spaces"+" Bottom: "+bottomSquare+"and a quickunion_sistem with "+quickUnionSystem.count()+"Spaces");
    }

    //opens the site (row, col) if it is not open already
    public void open(int row, int col)
    {
        if (row < 1 || row > size || col < 1 || col > size)
            throw new IllegalArgumentException("Can't find thoose coordenates");
        if (isOpen(row, col))
            return;
        //Make sure that the row and the column are in the range:
        matrixArray[size * (row-1) + (col - 1)] = true;
        percolationTopStart(row,col);
        upDownConnect(row,col);
        sidesConnect(row,col);
        percolationBottomStart(row,col);
        openSites++;
    }
    //Check if touches the up side of the system:
    private void percolationTopStart(int row, int col)
    {
        if(row == 1){
            quickUnionSystem.union(topSquare, size * (row - 1) + (col - 1));
            backwash.union(topSquare, size * (row - 1) + (col - 1));
        }
    }
    private void percolationBottomStart(int row, int col)
    {
        if (row == size){
            backwash.union(bottomSquare, size * (row - 1) + (col - 1));
        }
    }
    //Connect if possible to down/up square:
    private void upDownConnect(int row, int col)
    {
        if (row < size)
            if (isOpen(row + 1, col)){
                quickUnionSystem.union(row * size + (col - 1), (row - 1) * size + (col - 1));
                backwash.union(row * size + (col - 1), (row - 1) * size + (col - 1));
            }
        if (row > 1)
            if (isOpen(row - 1, col)) {
                quickUnionSystem.union((row - 2) * size + (col - 1), (row - 1) * size + (col - 1));
                backwash.union((row - 2) * size + (col - 1), (row - 1) * size + (col - 1));
            }
    }
    //Connect if possible to left/right square:
    private void sidesConnect(int row, int col)
    {
        if (col > 1)
            if (isOpen(row, col - 1)) {
                quickUnionSystem.union((row - 1) * size + (col - 2), (row - 1) * size + (col - 1));
                backwash.union((row - 1)* size + (col - 2), (row - 1) * size + (col -1));
            }
        if (col < size)
            if (isOpen(row, col + 1)) {
                quickUnionSystem.union((row - 1) * size + (col), (row - 1) * size + (col - 1));
                backwash.union((row - 1) * size + (col), (row - 1) * size + (col - 1));
            }
    }
    //is the site (row, col) open?
    public boolean isOpen(int row, int col)
    {
        if (row < 1 || row > size || col < 1 || col > size)
            throw new IllegalArgumentException("Can't find thoose coordenates");
        return matrixArray[(row - 1) * size + (col - 1)];
    }
    // is the site (row, col) full?
    public boolean isFull(int row, int col)
    {
        if (!isOpen(row, col))
            return false;
        if (row < 1 || row > size || col < 1 || col > size)
            throw new IllegalArgumentException("Can't find thoose coordenates");
        return quickUnionSystem.connected((row - 1) * size + (col - 1), topSquare);
    }
    // returns the number of open sites
    public int numberOfOpenSites(){
        return openSites;
    }
    // does the system percolate?
    public boolean percolates()
    {
        return backwash.connected(bottomSquare, topSquare);
    }
}