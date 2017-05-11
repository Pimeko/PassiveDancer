import java.awt.event.KeyEvent;

public class MusicManager
{
  public enum Musics
  {
    None, Menu, InGame, AllAchievements;
  }
  public static Musics currentMusicState;
  private static Music currentMusic;
  public static boolean enable_music;
  private static boolean has_pressed_M;
  
  public static void Initialize()
  {
    currentMusic = ContentManager.music_menu;
    currentMusicState = Musics.None;
    has_pressed_M = false;
  }
  
  public static void ChangeMusic(Musics newMusicState)
  {
    if(currentMusicState!=newMusicState)
    {
      currentMusic.Stop();
      switch(newMusicState)
      {
        case Menu:
          currentMusic = ContentManager.music_menu;
          currentMusic.Play(true);
          break;
        case InGame:
          currentMusic = ContentManager.music_in_game;
          currentMusic.Play(true);
          break;
        case AllAchievements:
          currentMusic = ContentManager.music_all_achievements;
          currentMusic.Play(true);
          break;
      }
      currentMusicState = newMusicState;
    }
  }
  
  public static void StopMusic()
  {
    currentMusic.Stop();
    currentMusicState = Musics.None;
  }
  
  public static void StopAllMusic()
  {
    for(int i = 0; i < ContentManager.all_musics.size(); i++)
      ContentManager.all_musics.get(i).Stop();
  }
  
  private static void AbleMusic()
  {
    if(enable_music)
      currentMusic.Play(true);
    else
      StopAllMusic();
  }
  
  public static void Update()
  {
    if(!StdDraw.isKeyPressed(KeyEvent.VK_M) && has_pressed_M)
    {
      enable_music = !enable_music;
      AbleMusic();
      has_pressed_M = false;
    }
    else if(StdDraw.isKeyPressed(KeyEvent.VK_M))
      has_pressed_M = true;
  }
  
  public static void Draw()
  {
    if(enable_music)
      StdDraw.text(0.1, 0.03, "(M) Music State : ON");
    else
      StdDraw.text(0.1, 0.03, "(M) Music State : OFF");
  }
}