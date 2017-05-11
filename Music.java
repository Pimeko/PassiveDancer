import java.applet.*;
import java.net.*;

public class Music
{
  String path;
  AudioClip music;
  
  public Music(String path)
  {
    this.path = path;
    try 
    {
      music = Applet.newAudioClip(new URL("file:" + path));
    }
    catch (MalformedURLException murle)
    {
      System.out.println("Could not load the music file " + path + " .");
    }
  }
  
  public void Play(boolean loop)
  {
    if(MusicManager.enable_music)
    {
      if(loop)
        music.loop();
      else
        music.play();
    }
  }
  
  public void Stop()
  {
    music.stop();
  }
}