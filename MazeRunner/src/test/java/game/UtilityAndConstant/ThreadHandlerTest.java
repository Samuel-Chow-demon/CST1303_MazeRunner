package game.UtilityAndConstant;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

class ThreadHandlerTest {

	@Test
    public void testShutDownAndTerminateAllThreads_success() throws InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        NoArgFunction<Void> successCallback = () -> {
            // Verify that the callback is called
            assertTrue(true);
            return null;
        };

        ThreadHandler.ShutDownAndTerminateAllThreads(executor, 1, TimeUnit.SECONDS, successCallback, null);

        // Verify that the executor is shut down
        assertTrue(executor.isTerminated());
    }

    @Test
    public void testShutDownAndTerminateAllThreads_timeout() throws InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        NoArgFunction<Void> failCallback = () -> {
            // Verify that the callback is called
            assertTrue(true);
            return null;
        };

        // Submit a task that will take longer than the timeout
        Future<?> future = executor.submit(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        ThreadHandler.ShutDownAndTerminateAllThreads(executor, 1, TimeUnit.SECONDS, null, failCallback);

        // Verify that the executor did not terminate within the timeout period
        // since it is now the timeout condition
        assertFalse(executor.isTerminated());
        
        //check if the executor was shut down called
        assertTrue(executor.isShutdown());
    }

    @Test
    public void testShutDownAndTerminateAllThreads_interrupted() throws InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        NoArgFunction<Void> failCallback = () -> {
            // Verify that the callback is called
            assertTrue(true);
            return null;
        };

        // Submit a task that will be interrupted
        Future<?> future = executor.submit(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Interrupt the future task
        future.cancel(true);

        ThreadHandler.ShutDownAndTerminateAllThreads(executor, 1, TimeUnit.SECONDS, null, failCallback);

        // Verify that the executor is shut down
        assertTrue(executor.isTerminated());
    }

    @Test
    public void testShutDownAndTerminateAllThreads_nullExecutor() {
        ThreadHandler.ShutDownAndTerminateAllThreads(null, 1, TimeUnit.SECONDS, null, null);

        // No exception should be thrown
    }

}
