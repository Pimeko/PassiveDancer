import java.util.*;

/* Animations are simply a succession of images. To display them, we set a timer between each
 * frame and we precise other info (looping, angle..) and the Update function executes the animation */
public class Animation
{
  ArrayList<Image2D> paths;
  int nb_frame, actual_frame, nb_flash_done;
  boolean loop, stop, stop_flash, freeze, is_visible;
  public boolean IsVisible() { return is_visible; }
  double x_fixed, y_fixed, delay_waited, delay_waited_flash, angle, delay;
  
  public Animation(ArrayList<Image2D> paths, double delay, boolean loop)
  {
    this.paths = paths;
    nb_frame = paths.size();
    this.delay = delay;
    this.loop = loop;
    delay_waited = 0;
    delay_waited_flash = 0;
    nb_flash_done = 0;
    actual_frame = 0;
    stop = false;
    stop_flash = false;
    angle = 0;
    freeze = false;
    is_visible = true;
  }
  
  public Animation(ArrayList<Image2D> paths, double delay, boolean loop, double angle)
  {
    this.paths = paths;
    nb_frame = paths.size();
    this.delay = delay;
    this.loop = loop;
    delay_waited = 0;
    delay_waited_flash = 0;
    nb_flash_done = 0;
    actual_frame = 0;
    stop = false;
    stop_flash = false;
    this.angle = angle;
    freeze = false;
    is_visible = true;
  }
  
  public Animation(ArrayList<Image2D> paths, double delay, boolean loop, double x, double y)
  {
    this.paths = paths;
    nb_frame = paths.size();
    this.delay = delay;
    this.loop = loop;
    x_fixed = x;
    y_fixed = y;
    delay_waited = 0;
    delay_waited_flash = 0;
    nb_flash_done = 0;
    actual_frame = 0;
    stop = false;
    stop_flash = false;
    angle = 0;
    freeze = false;
    is_visible = true;
  }
  
  public Animation(ArrayList<Image2D> paths, double delay, boolean loop, double x, double y, double angle)
  {
    this.paths = paths;
    nb_frame = paths.size();
    this.delay = delay;
    this.loop = loop;
    x_fixed = x;
    y_fixed = y;
    delay_waited = 0;
    delay_waited_flash = 0;
    nb_flash_done = 0;
    actual_frame = 0;
    stop = false;
    stop_flash = false;
    this.angle = angle;
    freeze = false;
    is_visible = true;
  }
  
  // Fixed
  public void Draw(boolean animation)
  {
    Draw(x_fixed, y_fixed, animation);
  }
  
  public void Draw(double angle, boolean animation)
  {
    Draw(x_fixed, y_fixed, angle, animation);
  }
  
  public void Draw(double x, double y, boolean animation)
  {
    if(!stop)
    {
      is_visible = true;
      if(!animation)
        StdDraw.picture(x, y, paths.get(actual_frame).path, angle);
      else
      {
        StdDraw.picture(x, y, paths.get(actual_frame).path, angle);
        
        if(delay_waited >= delay)
        {
          NextFrame();
          delay_waited = 0;
          if(!loop)
            stop = (actual_frame == 0);
        }
        else
          delay_waited+=0.5;
      }
    }
  }
  
  public void Draw(double x, double y, double angle, boolean animation)
  {
    if(!stop)
    {      
      is_visible = true;
      if(!animation)
        StdDraw.picture(x, y, paths.get(actual_frame).path, angle);
      else
      {
        StdDraw.picture(x, y, paths.get(actual_frame).path, angle);
        
        if(delay_waited >= delay)
        {
          NextFrame();
          delay_waited = 0;
          if(!loop)
            stop = (actual_frame == 0);
        }
        else
          delay_waited+=0.5;
      }
    }
  }
  
  public void StartFlash()
  {
    stop_flash = false;
    actual_frame = 0;
    nb_flash_done = 0;
    delay_waited_flash = 0;
  }
  
  // Returns false if over
  public boolean Flash(double x, double y, double delay_flash, int nb_flash)
  { 
    return Flash(x, y, delay_flash, nb_flash, angle);
  }
  
  public boolean Flash(double x, double y, double delay_flash, int nb_flash, double angle)
  { 
    if(!stop_flash)
    {
      if(actual_frame == 0)
      {
        StdDraw.picture(x, y, paths.get(actual_frame).path, angle);
        is_visible = true;
      }
      else
        is_visible = false;
      
      if(delay_waited_flash >= delay_flash)
      {
        nb_flash_done++;
        if(actual_frame > 0)
          actual_frame = 0;
        else
          actual_frame++;
        delay_waited_flash = 0;
      }
      else
        delay_waited_flash++;
    }
    if(nb_flash_done == 2*nb_flash)
      stop_flash = true;
    if(nb_flash == 0)
      stop_flash = false;
    return !stop_flash;
  }
  
  public void StopFlash()
  {
    stop_flash = true;
  }
  
  private void NextFrame()
  {
    if(actual_frame < nb_frame-1)
      actual_frame++;
    else
      actual_frame = 0;
  }
  
  public boolean IsFinished()
  {
    return stop;
  }
}