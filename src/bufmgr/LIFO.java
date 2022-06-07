/* File LIFO.java */

package bufmgr;

import diskmgr.*;
import global.*;

  /**
   * class LIFO is a subclass of class Replacer using LIFO
   * algorithm for page replacement
   */
class LIFO extends  Replacer {

  /**
   * private field
   * An array to hold number of frames in the buffer pool
   */

    private int  frames[];
 
  /**
   * private field
   * number of frames used
   */   
  private int nframes;

/**
 * Adding the frame with given frame number to buffer pool
 * putting it in front of the list
 *
 * @param	frameNo	 the frame number
 * @see 	BufMgr
 */

private void update(int frameNo)
{
    int index;
    int numBuffers=mgr.getNumBuffers();
   for ( index=0; index < numBuffers; ++index )
       if ( frames[index] < 0  ||  frames[index] == frameNo )
           break;


   // If buffer pool is not yet full, add this frame to it...
   if ( frames[index] < 0 )
       frames[index] = frameNo;

   int frame = frames[index];
   while ( index-- >0)
     frames[index+1] = frames[index];
       
 frames[0] = frame;
}

  /**
   * Calling super class the same method
   * Initializing the frames[] with number of buffer allocated
   * by buffer manager
   * set number of frame used to zero
   *
   * @param	mgr	a BufMgr object
   * @see	BufMgr
   * @see	Replacer
   */
    public void setBufferManager( BufMgr mgr )
     {
        super.setBufferManager(mgr);
        int numBuffers = mgr.getNumBuffers();
	       frames = new int [ numBuffers];
      for ( int index = 0; index < numBuffers; ++index )
        frames[index] = -index;
	nframes = 0;
     }

/* public methods */

  /**
   * Class constructor
   * Initializing frames[] pinter = null.
   */
    public LIFO(BufMgr mgrArg)
    {
      super(mgrArg);
      frames = null;
    }
  
  /**
   * calll super class the same method
   * pin the page in the given frame number 
   * move the page to the end of list  
   *
   * @param	 frameNo	 the frame number to pin
   * @exception  InvalidFrameNumberException
   */
 public void pin(int frameNo) throws InvalidFrameNumberException
 {
    super.pin(frameNo);

    // update(frameNo);
    
 }

  /**
   * Finding a free frame in the buffer pool
   * or choosing a page to replace using LIFO policy
   *
   * @return 	return the frame number
   *		return -1 if failed
   */

 public int pick_victim()
 {
    int numBuffers = mgr.getNumBuffers();
    int i, frame;

      for ( i = 0; i < numBuffers; ++i )
        if (frames[i] < 0) {
          if ( i == 0 )
              frames[i] = 0;
          else
              frames[i] *= -1;
          frame = frames[i];
          state_bit[frame].state = Pinned;
          (mgr.frameTable())[frame].pin();
          update(frame);
          return frame;
      }
      
      //if no free frame is found, return frame at top of stack
      for ( i = 0; i < numBuffers; ++i ) {
        frame = frames[i];
        if (state_bit[frame].state != Pinned) {
          state_bit[frame].state = Pinned;
          (mgr.frameTable())[frame].pin();
          update(frame);
          return(frame);
        }
      }

      return -1;
 }
 
  /**
   * get the page replacement policy name
   *
   * @return	return the name of replacement policy used
   */  
    public String name() { return "LIFO"; }
 
  /**
   * print out the information of frame usage
   */  
 public void info()
 {
    super.info();

    System.out.print( "LIFO REPLACEMENT");
    
    for (int i = 0; i < nframes; i++) {
        if (i % 5 == 0)
	System.out.println( );
	System.out.print( "\t" + frames[i]);
        
    }
    System.out.println();
 }
  
}



