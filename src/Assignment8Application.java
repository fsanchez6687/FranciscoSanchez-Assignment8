import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Assignment8Application {
    public static void main(String[] args) {
        Assignment8 assignment8 = new Assignment8();

        List<Integer> allNumbers = Collections.synchronizedList(new ArrayList<>(1000));

        ExecutorService executor = Executors.newCachedThreadPool();

        List<CompletableFuture<Void>> counts = new ArrayList<>(1000);

        for (int i = 0; i < 1000; i++) {
            CompletableFuture<Void> count = CompletableFuture.supplyAsync(() -> assignment8.getNumbers(), executor)
                    .thenAccept(numbers -> allNumbers.addAll(numbers));
            counts.add(count);
        }

        while (counts.stream().filter(CompletableFuture::isDone).count() < 1000)

        {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }

        System.out.println("Obtained 1,000,000 numbers");
        System.out.println("Records counted: " + allNumbers.size());


        Map<Integer, Integer> output = allNumbers.stream()
                .collect(Collectors.toMap(i -> i, i -> 1, (oldValue, newValue) -> oldValue + 1));
        System.out.println(output);

    }
}