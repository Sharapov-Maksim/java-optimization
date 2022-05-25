package nsu.fit.javaperf;

public class TransactionProcessor {

    public void processTransaction(int txNum) throws Exception{
        System.err.println("Processing tx: " + txNum);
        int sleep = (int) (Math.random() * 1000);
        Thread.sleep(sleep);
        System.err.println(String.format("tx: %d completed", txNum));
    }

    public static void main(String[] args) throws Exception{
        TransactionProcessor tp = new TransactionProcessor();
        TetsClass t = new TetsClass();
        int tx = 0;
        tp.processTransaction(++tx);
        tp.processTransaction(++tx);
        tp.processTransaction(++tx);
    }

    public static class TetsClass {
        public int i;

        TetsClass() {
            this(1);
            System.out.println("Basic Constructor end");
        }
        TetsClass(int i){
            this.i = i;
            System.out.println("Complex Constructor end");
        }
    }
}
