package game.UtilityAndConstant;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class ThreadHandler {

	public static void ShutDownAndTerminateAllThreads(ExecutorService executor, long timout, TimeUnit unit,
													  NoArgFunction<Void> funcSuccessCallBack,
													  NoArgFunction<Void> funcFailCallBack)
	{
		if (executor != null)
		{
			// Acknowledge all the threads is about to be terminated
			executor.shutdown();
			try 
			{
				// Wait for existing tasks to terminate
			    if (!executor.awaitTermination(timout, unit)) 
			    {
			    	 // Cancel currently executing tasks and shut down immediately
			        executor.shutdownNow();
			    }
			    
			    if (funcSuccessCallBack != null)
			    {
			    	funcSuccessCallBack.apply();
			    }
			} 
			catch (InterruptedException ex) 
			{
				// Call again the thread shut down
			    executor.shutdownNow();
			    Thread.currentThread().interrupt();
			    
			    if (funcFailCallBack != null)
			    {
			    	funcFailCallBack.apply();
			    }
			}
		}
	}
}
