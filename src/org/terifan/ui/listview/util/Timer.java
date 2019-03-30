package org.terifan.ui.listview.util;

import static java.lang.Thread.sleep;


/**
 * Simple Timer implementation with the ability to pause.
 */
public class Timer implements AutoCloseable
{
	private boolean mPaused;
	private Runnable mRunnable;
	private long mPauseAt;
	private long mDelay;
	private WorkerThread mThread;


	/**
	 * Creates a Timer that repeatedly calls a Runnable
	 *
	 * @param aRunnable
	 * the Runnable being called
	 */
	public Timer(Runnable aRunnable)
	{
		mRunnable = aRunnable;
		mPauseAt = Long.MAX_VALUE;
	}


	public synchronized Timer start()
	{
		if (mThread == null)
		{
			mThread = new WorkerThread();
			mThread.start();
		}
		return this;
	}


	public synchronized Timer stop()
	{
		if (mThread != null)
		{
			mThread.mCancel = true;
			mThread.wakeUp();
			mThread = null;
		}
		return this;
	}


	/**
	 * Sets an optional delay between calls to the Runnable.
	 *
	 * @param aDelay
	 * time in milliseconds
	 */
	public Timer setDelay(long aDelay)
	{
		mDelay = aDelay;
		return this;
	}


	/**
	 * The timer will be paused at the time specified.
	 *
	 * @param aPauseAt
	 * time in milliseconds
	 */
	public Timer setPauseAt(long aPauseAt)
	{
		mPauseAt = aPauseAt;
		return this;
	}


	/**
	 * Pauses or un-pauses the Timer.
	 */
	public synchronized Timer setPaused(boolean aPaused)
	{
		mPaused = aPaused;
		if (mThread != null && !mPaused)
		{
			mThread.wakeUp();
		}
		return this;
	}


	/**
	 * Stops this Timer. Same as calling the <code>stop</code> method.
	 */
	@Override
	public void close() throws Exception
	{
		stop();
	}


	private class WorkerThread extends Thread
	{
		boolean mCancel;


		public WorkerThread()
		{
			setDaemon(true);
		}


		void wakeUp()
		{
			synchronized (this)
			{
				notify();
			}
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
						synchronized (this)
						{
							wait(1000);
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
	};
}
