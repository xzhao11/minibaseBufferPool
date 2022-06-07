/* File LFU.java */

package bufmgr;

import diskmgr.*;
import global.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Comparator;

  /**
   * class LFU is a subclass of class Replacer using LFU
   * algorithm for page replacement
   */
class LFU extends  Replacer {

  /**
   * private field
   * An array to hold number of frames in the buffer pool
   */

    private int  frames[];
    private Map<Integer, Integer> counts;
  /**
   * private field
   * number of frames used
   */   
  private int  nframes;

  /**
   * This pushes the given frame to the end of the list.
   * @param frameNo	the frame number
   */
  private void update(int frameNo)
  {


     int index;
     for ( index=0; index < nframes; ++index )
        if ( frames[index] == frameNo )
            break;

    while ( ++index < nframes )
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
    public void setBufferManager( BufMgr mgr )
     {
        super.setBufferManager(mgr);
	frames = new int [ mgr.getNumBuffers() ];
	nframes = 0;

    counts = new HashMap<>();
    }

/* public methods */

  /**
   * Class constructor
   * Initializing frames[] pinter = null.
   */
    public LFU(BufMgr mgrArg)
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
    counts.putIfAbsent(frameNo, 0);
    counts.put(frameNo, counts.get(frameNo)+1);
    // update(frameNo);
    
 }

  /**
   * Finding a free frame in the buffer pool
   * or choosing a page to replace using LFU policy
   *
   * @return 	return the frame number
   *		return -1 if failed
   */

public int pick_victim()
{
   int numBuffers = mgr.getNumBuffers();
   int frame;
    if ( nframes < numBuffers ) {
        frame = nframes++;
        frames[frame] = frame;
        state_bit[frame].state = Pinned;
        (mgr.frameTable())[frame].pin();
        counts.putIfAbsent(frame, 0);
        counts.put(frame, counts.get(frame)+1);
        return frame;
    }
    int min_count = Integer.MAX_VALUE;
    int least_frequent_page = -1;
    for ( int i = 0; i < numBuffers; ++i ) {
        frame = frames[i];
        if (counts.get(frame) < min_count) {
            if ( state_bit[frame].state != Pinned ) {
                least_frequent_page = frame;
                min_count = counts.get(frame);
            }
        }
    }
    if (least_frequent_page != -1) {
        state_bit[least_frequent_page].state = Pinned;
        (mgr.frameTable())[least_frequent_page].pin();
        // update(least_frequent_page);
        counts.put(least_frequent_page, 1);
    }
                    
    return least_frequent_page;
}
 
  /**
   * get the page replacement policy name
   *
   * @return	return the name of replacement policy used
   */  
    public String name() { return "LFU"; }
 
  /**
   * print out the information of frame usage
   */  
 public void info()
 {
    super.info();

    System.out.print( "LFU REPLACEMENT");
    
    for (int i = 0; i < nframes; i++) {
        if (i % 5 == 0)
	System.out.println( );
	System.out.print( "\t" + frames[i]);
        
    }
    System.out.println();
 }
  
}



