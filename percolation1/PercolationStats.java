import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double CONFIDENCE_95 = 1.96;
    private double[] monteCarloSimulationResults;
    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials){
        if(n < 1 || trials < 0)
            throw new IllegalArgumentException("Parameters not valid :I");
        monteCarloSimulationResults = new double[trials];
        simulationAlgorithm(n, trials);
    }
    //run the simulation trials times, with same size sqares
    private void simulationAlgorithm(int n, int trials){
        for(int i = 0; i < trials; i++){
            monteCarloSimulationResults[i] = algorithmHelper(n);
        }
    }
    private double algorithmHelper(int n){
        int spaces_opened = 0;
        Percolation percolation = new Percolation(n);
        while(!percolation.percolates()){
            int col = StdRandom.uniform(1, n+1);
            int row = StdRandom.uniform(1, n+1);
            if (!percolation.isOpen(row, col)){
                percolation.open(row, col);
                spaces_opened++;
            }
        }
        return (double) spaces_opened / ((double) (n * n));
    }
    // sample mean of percolation threshold
    public double mean(){
        double mean = StdStats.mean(monteCarloSimulationResults);
        return mean;
    }
    // sample standard deviation of percolation threshold
    public double stddev(){
        double deviation = StdStats.stddev(monteCarloSimulationResults);
        return deviation;
    }
    // low endpoint of 95% confidence interval
    public double confidenceLo(){
        double deviation = (CONFIDENCE_95 * stddev()) / (Math.sqrt(monteCarloSimulationResults.length));
        return mean() - deviation; 
    }
    // high endpoint of 95% confidence interval
    public double confidenceHi(){
        double deviation = (CONFIDENCE_95 * stddev()) / (Math.sqrt(monteCarloSimulationResults.length));
        return mean() + deviation; 
    }

   // test client (see below)
   public static void main(String[] args){
        PercolationStats stats = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        System.out.println("mean************************** " + stats.mean());
        System.out.println("stddev************************ " + stats.stddev());
        System.out.println("95% confidence interval******* " + stats.confidenceLo() + ", " + stats.confidenceHi());
   }
}
