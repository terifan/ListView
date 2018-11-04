package org.terifan.ui.listview;

//import java.awt.Rectangle;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import javax.swing.AbstractAction;
//import javax.swing.JComponent;
//import javax.swing.Timer;
//
//
//public class SmoothScrollController
//{
//	private final JComponent mOwner;
//	private Timer mScrollTimer;
//	private double mScrollVelocity;
//	private double mScrollFraction;
//
//
//	public SmoothScrollController(JComponent aOwner, int a, int b, int c)
//	{
//		mOwner = aOwner;
//	}
//
//
//	public synchronized void smoothScroll(double aPreciseWheelRotation)
//	{
//		mScrollVelocity += 60 * aPreciseWheelRotation;
//
//		if (mScrollTimer == null)
//		{
//			ActionListener task = new AbstractAction()
//			{
//				@Override
//				public void actionPerformed(ActionEvent aEvent)
//				{
//					mScrollFraction += mScrollVelocity;
//
//					int scrollInc = (int)mScrollFraction;
//
//					if (scrollInc != 0)
//					{
//						Rectangle current = mOwner.getVisibleRect();
//						current.y += scrollInc;
//
//						mScrollFraction -= scrollInc;
//
//						mOwner.scrollRectToVisible(current);
//					}
//
//					mScrollVelocity *= 0.85;
//
//					if (mScrollVelocity < 0 && mScrollFraction + mScrollVelocity > -1 || mScrollVelocity > 0 && mScrollFraction + mScrollVelocity < 1)
//					{
//						synchronized (SmoothScrollController.this)
//						{
//							mScrollTimer.stop();
//							mScrollFraction = 0;
//							mScrollVelocity = 0;
//							mScrollTimer = null;
//						}
//					}
//				}
//			};
//
//			mScrollTimer = new Timer(10, task);
//			mScrollTimer.setInitialDelay(0);
//			mScrollTimer.start();
//		}
//	}
//}


import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.Timer;


/**
 * Animation controller for smooth scrolling a component viewing rectangle.
 *
 * <pre>
 * smoothScroll = new SmoothScrollController(scrollPane, 10, 256, 500);
 * scrollPane.addMouseWheelListener(new MouseAdapter()
 * {
 *	@Override
 *	public void mouseWheelMoved(MouseWheelEvent aEvent)
 *	{
 *		smoothScroll.smoothScroll(aEvent.getPreciseWheelRotation());
 *	}
 * });
 * </pre>
 *
 * @see
 * https://stackoverflow.com/questions/13550569/jscrollpane-smooth-scrolling
 */
public class SmoothScrollController
{
	private final JComponent mOwner;
	private Timer mScrollTimer;

	private int mTimeBetweenIterations;
	private int mAnimationDistance;
	private long mAnimationTime;
	private Rectangle mCurrentBounds;
	private int mStartOffset;
	private int mTargetOffset;
	private long mStartTime;
	private long mTargetElapsedTime;
	private double mWheelRotation;
	private double mLastWheelRotation;
	private long mOperationStartTime;


	/**
	 * Create the animation controller.
	 *
	 * @param aOwner
	 *   the object to scrolled
	 * @param aTimeBetweenIterations
	 *   time in MS between redraws, typically 10 ms
	 * @param aAnimationDistance
	 *   distance to travel for a single scroll invocation, typically row height + vertical padding
	 * @param aAnimationTime
	 *   the time for a single scroll invocation, typically 500 ms
	 */
	public SmoothScrollController(JComponent aOwner, int aTimeBetweenIterations, int aAnimationDistance, int aAnimationTime)
	{
		mOwner = aOwner;
		mTimeBetweenIterations = aTimeBetweenIterations;
		mAnimationDistance = aAnimationDistance;
		mAnimationTime = aAnimationTime;
	}


	/**
	 * Scroll the view.
	 *
	 * @param aWheelRotation
	 *   the rotation amount, usually -1.0 for scrolling up and 1.0 for scrolling down.
	 */
	public void smoothScroll(double aWheelRotation)
	{
		synchronized (SmoothScrollController.class)
		{
			mWheelRotation = aWheelRotation;
		}

		if (mScrollTimer == null)
		{
			mOperationStartTime = System.currentTimeMillis();

			ActionListener task = new AbstractAction()
			{
				@Override
				public void actionPerformed(ActionEvent aEvent)
				{
//					System.out.println(System.currentTimeMillis() - mOperationStartTime);
					mOperationStartTime = System.currentTimeMillis();

					if (mWheelRotation != 0)
					{
						mCurrentBounds = mOwner.getVisibleRect();

						if (mStartTime != 0 && Math.signum(mLastWheelRotation) == Math.signum(mWheelRotation))
						{
							mTargetOffset += (int)(mAnimationDistance * mWheelRotation);
						}
						else
						{
							mTargetOffset = mCurrentBounds.y + (int)(mAnimationDistance * mWheelRotation);
						}

						mStartTime = System.nanoTime() - (mTimeBetweenIterations * 1000000); // Make the animation move on the first iteration
						mTargetElapsedTime = mAnimationTime * 1000000;
						mStartOffset = mCurrentBounds.y;

						mLastWheelRotation = mWheelRotation;
						mWheelRotation = 0;
					}

					long timeSinceStart = System.nanoTime() - mStartTime;
					double percentComplete = Math.min(1.0, timeSinceStart / (double)mTargetElapsedTime);
					double factor = 1.0 - Math.pow(1.0 - percentComplete, 3);

					mCurrentBounds.y = (int)Math.round(mStartOffset + (mTargetOffset - mStartOffset) * factor);
					mOwner.scrollRectToVisible(mCurrentBounds);

					if (timeSinceStart >= mTargetElapsedTime)
					{
						synchronized (SmoothScrollController.class)
						{
							if (mWheelRotation == 0)
							{
								mStartTime = 0;
								mScrollTimer.stop();
								mScrollTimer = null;
							}
						}
					}
				}
			};

			mScrollTimer = new Timer(mTimeBetweenIterations, task);
			mScrollTimer.setInitialDelay(0);
			mScrollTimer.start();
		}
	}
}
