public class Counter {
    private int count;
    private double sum;

    // CONSTRUCTOR
    public Counter(int count, double sum) {
        this.count = count;
        this.sum = sum;
    }

    public void inc(double delta){
        this.sum = this.sum + delta;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void plusOne(){
        this.count++;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public double avarage(){
        return sum/count;
    }
}
