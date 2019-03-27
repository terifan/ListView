package org.terifan.ui.listview.util;

import static java.lang.Thread.sleep;


/**
 * Simple Timer implementation with the ability to pause.
 */
public class Timer extends Thread implements AutoCloseable
{
	private boolean mPaused;
	private boolean mCancel;
	private Runnable mRunnable;
	private long mPauseAt;
	private long mDelay;


	/**
	 * Creates a Timer that repeatedly calls a Runnable
	 * 
	 * @param aRunnable 
	 *   the Runnable being called
	 */
	public Timer(Runnable aRunnable)
	{
		setDaemon(true);

		mRunnable = aRunnable;
		mPauseAt = Long.MAX_VALUE;
	}


	public void setRunnable(Runnable aRunnable)
	{
		mRunnable = aRunnable;
	}


	/**
	 * Sets an optional delay between calls to the Runnable.
	 * 
	 * @param aDelay 
	 *    time in milliseconds
	 */
	public void setDelay(long aDelay)
	{
		mDelay = aDelay;
	}


	/**
	 * The timer will be paused at the time specified.
	 * 
	 * @param aPauseAt
	 *    time in milliseconds
	 */
	public void setPauseAt(long aPauseAt)
	{
		mPauseAt = aPauseAt;
	}


	/**
	 * Pauses or un-pauses the Timer.
	 */
	public void setPaused(boolean aPaused)
	{
		mPaused = aPaused;
		synchronized (Runnable.class)
		{
			Runnable.class.notify();
		}
	}


	/**
	 * Cancels this Timer.
	 */
	public void cancel()
	{
		mCancel = true;
		synchronized (Runnable.class)
		{
			Runnable.class.notify();
		}
	}


	/**
	 * Cancels this Timer.
	 */
	@Override
	public void close() throws Exception
	{
		cancel();
	}


	@Override
	public void run()
	{
		while (!mCancel)
		{
			while (mPaused)
			{
				try
				{
					synchronized (Runnable.class)
					{
						Runnable.class.wait(1000);
					}
				}
				catch (InterruptedException e)
				{
				}
			}

			if (mCancel)
			{
				return;
			}

			try
			{
				mRunnable.run();
			}
			catch (Error | Exception e)
			{
			}

			if (mDelay > 0)
			{
				try
				{
					sleep(mDelay);
				}
				catch (InterruptedException e)
				{
				}
			}

			if (System.currentTimeMillis() > mPauseAt)
			{
				mPaused = true;
				mPauseAt = Long.MAX_VALUE;
			}
		}
	}
}
