package au.edu.sydney.soft3202.task1;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
public class SampleBenchmark {

    @State(Scope.Thread)
    public static class MyBasket {
        ShoppingBasket sb = null;
        @Setup(Level.Iteration)
        public void doSetup() {
            sb = new ShoppingBasket();
            sb.addItem("apple", 10);
            sb.addNewItem("grapes", 9.25);
            sb.addItem("grapes", 5);
        }
        @TearDown(Level.Iteration)
        public void doTeardown() {
            sb = null;
        }
    }

    @Fork(value=1)
    @Warmup(iterations=1)
    @Measurement(iterations = 1)
    @Benchmark @BenchmarkMode(Mode.Throughput) @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void addNewItem(Blackhole bh, MyBasket b) {
        b.sb.addItem("apple", 1);
    }

    @Fork(value=1)
    @Warmup(iterations=1)
    @Measurement(iterations = 1)
    @Benchmark @BenchmarkMode(Mode.Throughput) @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void removeItem(Blackhole bh, MyBasket b) {
        b.sb.removeItem("apple", 1);
    }


    @Fork(value=1)
    @Warmup(iterations=1)
    @Measurement(iterations = 1)
    @Benchmark @BenchmarkMode(Mode.Throughput) @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void addNewItemName(Blackhole bh, MyBasket b) {
        b.sb.addNewItem("mango", 4.25);
    }

    @Fork(value=1)
    @Warmup(iterations=1)
    @Measurement(iterations = 1)
    @Benchmark @BenchmarkMode(Mode.Throughput) @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void removeNewItemName(Blackhole bh, MyBasket b) {
        b.sb.deleteItem("grapes");
    }


    @Fork(value=1)
    @Warmup(iterations=1)
    @Measurement(iterations = 1)
    @Benchmark @BenchmarkMode(Mode.Throughput) @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void addNewItemNameIncrementTenTimesAndRemovingItem(Blackhole bh, MyBasket b) {
        b.sb.addNewItem("chips", 2.5);
        b.sb.addItem("chips", 1);
        b.sb.addItem("chips", 1);
        b.sb.addItem("chips", 1);
        b.sb.addItem("chips", 1);
        b.sb.addItem("chips", 1);
        b.sb.addItem("chips", 1);
        b.sb.addItem("chips", 1);
        b.sb.addItem("chips", 1);
        b.sb.addItem("chips", 1);
        b.sb.addItem("chips", 1);
//        for (int x = 0; x <= 10; x++) {
//            b.sb.addItem("chips", 1);
//        }
        b.sb.deleteItem("chips");
    }

    //FIX FOR OLD BUG (KEPT REMOVING ITEM TILL ERROR)
    //    @Fork(value=1)
//    @Warmup(iterations=1)
//    @Measurement(iterations = 1)
//    @Benchmark @BenchmarkMode(Mode.Throughput) @OutputTimeUnit(TimeUnit.NANOSECONDS)
//    public void shopRemoveItem(Blackhole bh, MyBasket b) {
////        System.out.println("total cost :" + b.sb.getCartTotal() +" apple qty:  " + b.sb.getCart().get(0).getQty());
////        bh.consume(b);
//        if (!b.sb.containsItem("apple")) {
//            b.sb.removeItem("apple", 1);
//        }
////        b.sb.removeItem("apple", 1);
//
//
//    }


}
