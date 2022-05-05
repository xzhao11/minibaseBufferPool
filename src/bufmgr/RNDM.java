/* File Random.java */

package bufmgr;

import diskmgr.*;
import global.*;
import java.util.Random;

  /**
   * class Random is a subclass of class Replacer that pick random victom
   *  for page replacement
   */
class RNDM extends Replacer {

  /**
   * private field
   * An array to hold number of frames in the buffer pool
   */
  private int frames[];
 
  /**
   * private field
   * number of frames used
   */   
  private int nframes;

  /**
   * This pushes the given frame to the end of the list.
   * @param frameNo	the frame number
   */
  private void update(int frameNo) {
    int index;
    for (index = 0; index < nframes; ++index)
      if (frames[index] == frameNo)
        break;

    while (++index < nframes)
      frames[index-1] = frames[index];
      frames[nframes-1] = frameNo;
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
  public void setBufferManager(BufMgr mgr) {
    super.setBufferManager(mgr);
	  frames = new int[mgr.getNumBuffers()];
	  nframes = 0;
  }

/* public methods */

  /**
   * Class constructor
   * Initializing frames[] pointer = null.
   */
  public RNDM(BufMgr mgrArg) {
    super(mgrArg);
    frames = null;
  }
  
  /**
   * call super class the same method
   * pin the page in the given frame number 
   * move the page to the end of list  
   *
   * @param	 frameNo	 the frame number to pin
   * @exception  InvalidFrameNumberException
   */
  public void pin(int frameNo) throws InvalidFrameNumberException {
    super.pin(frameNo);
    update(frameNo);
  }

  /**
   * Finding a free frame in the buffer pool
   * or choosing a page to replace using Random policy
   *
   * @return 	return the frame number
   *		return -1 if failed
   */
  public int pick_victim() {
    int numBuffers = mgr.getNumBuffers();
    int frame;

    if (nframes < numBuffers) {
      frame = nframes++;
      frames[frame] = frame;
      state_bit[frame].state = Pinned;
      (mgr.frameTable())[frame].pin();
      return frame;
    } else {
      Random rand = new Random();
      int[] seenFrames = new int[nframes];
      int numSeenFrames = 0;

      while (numSeenFrames < nframes) {
        frame = rand.nextInt(nframes);      
        if (state_bit[frame].state != 0) {
          state_bit[frame].state = Pinned;
          (mgr.frameTable())[frame].pin();
          update(frame);
          return frame;
        }

        if (seenFrames[frame] == 0) {
          seenFrames[frame] = 1;
          numSeenFrames++;
        }
      }
    }

    return -1;
  }
 
  /**
   * get the page replacement policy name
   *
   * @return	return the name of replacement policy used
   */  
  public String name() { return "Random"; }
 
  /**
   * print out the information of frame usage
   */  
  public void info() {
    super.info();
    System.out.print( "Random REPLACEMENT");
      
    for (int i = 0; i < nframes; i++) {
      if (i % 5 == 0)
      System.out.println( );
      System.out.print( "\t" + frames[i]);      
    }
    System.out.println();
  }
}
