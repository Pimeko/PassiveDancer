import java.util.*;
import java.awt.Font;

public class ContentManager
{
  /* We load all the images and sounds here so that their adress is stored once and for all 
   * Then, thanks to the objects Image2D and Music, we can get their information easily (path, size, ...) */
  
  /* Images */
  public static String image_folder = "Images/";
  public static Image2D title_background = new Image2D(image_folder + "title_background.png");
  public static Image2D title_menu = new Image2D(image_folder + "title_menu.png");
  public static Image2D grid_empty = new Image2D(image_folder + "grid_empty.png");
  public static int WindowWidth = title_background.Width(), WindowHeight = title_background.Height();
  public static Image2D background = new Image2D(image_folder + "background.png");
  public static Image2D gameover_background = new Image2D(image_folder + "gameover_background.png");
  public static Image2D background_pause = new Image2D(image_folder + "background_pause.png");
  public static Image2D menu_pause = new Image2D(image_folder + "menu_pause.png");
  public static Image2D menu_pause_1 = new Image2D(image_folder + "menu_pause_1.png");
  public static Image2D menu_pause_2 = new Image2D(image_folder + "menu_pause_2.png");
  
  public static Image2D ship = new Image2D(image_folder + "ship.png");
  public static Image2D goldenship = new Image2D(image_folder + "goldenship.png");
  public static Image2D bullet = new Image2D(image_folder + "bullet.png");
  public static Image2D bullet_alien = new Image2D(image_folder + "bullet_alien.png");
  public static Image2D bullet_special = new Image2D(image_folder + "bullet_special.png");
  public static Image2D shield = new Image2D(image_folder + "shield.png");
  public static Image2D rose = new Image2D(image_folder + "rose.png");
  public static Image2D rose_trainee = new Image2D(image_folder + "rose_trainee.png");
  
  public static Image2D background_all_achievements = new Image2D(image_folder + "background_all_achievements.png");
  public static Image2D star_all_achievements = new Image2D(image_folder + "star_all_achievements.png");
  public static Image2D door_stanley = new Image2D(image_folder + "door.png");
  
  public static Image2D star = new Image2D(image_folder + "star.png");
  public static Image2D fog = new Image2D(image_folder + "fog.png");
  public static Image2D cloud = new Image2D(image_folder + "cloud.png");
  public static Image2D earth = new Image2D(image_folder + "earth.png");
  
  public static Image2D bonus_laser =  new Image2D(image_folder + "bonus_laser.png");
  public static Image2D bonus_shield =  new Image2D(image_folder + "bonus_shield.png");
  public static Image2D bonus_slow =  new Image2D(image_folder + "bonus_slow.png");
  public static Image2D bonus_life =  new Image2D(image_folder + "bonus_life.png");
  public static Image2D clock_background =  new Image2D(image_folder + "clock/" + "background.png");
  public static Image2D clock_small_aiguille =  new Image2D(image_folder + "clock/" + "small_aiguille.png");
  public static Image2D clock_big_aiguille =  new Image2D(image_folder + "clock/" + "big_aiguille.png");
  
  public static Image2D saving =  new Image2D(image_folder + "saving.png");
  
  public static Image2D BonusTypeToImage(Bonus.BonusType bonusType)
  {
    Image2D r = null;
    switch(bonusType)
    {
      case Destructor:
        r = bonus_laser;
        break;
        
      case Shield:
        r = bonus_shield;
        break;
        
      case Slow:
        r = bonus_slow;
        break;
        
      case Life:
        r = bonus_life;
        break;
        
      default:
        r = bonus_laser;
        break;
    }
    
    return r;
  }
  /* Images */
  
  /* Fonts */
  public static Font font = new Font("font", 0, 20);
  public static void SetFontBlack() { StdDraw.setPenColor(StdDraw.BLACK); }
  public static void SetFontWhite() { StdDraw.setPenColor(StdDraw.WHITE); }
  public static void SetFontRed() { StdDraw.setPenColor(StdDraw.RED); }
  /* Fonts */
  
  /* Animations && Achievements */
  private static String anim_folder = "Animation/";
  public static ArrayList<Image2D> menu_background = new ArrayList<Image2D>();
  public static ArrayList<Image2D> ship_move = new ArrayList<Image2D>();
  public static ArrayList<Image2D> ship_destructor_move = new ArrayList<Image2D>();
  public static ArrayList<Image2D> ship_death = new ArrayList<Image2D>();
  public static ArrayList<Image2D> ship_evolution = new ArrayList<Image2D>();
  public static ArrayList<Image2D> goldenship_move = new ArrayList<Image2D>();
  public static ArrayList<Image2D> goldenship_destructor_move = new ArrayList<Image2D>();
  public static ArrayList<Image2D> goldenship_death = new ArrayList<Image2D>();
  public static ArrayList<ArrayList<Image2D>> alien_move = new ArrayList<ArrayList<Image2D>>();
  public static ArrayList<Image2D> special_alien_move = new ArrayList<Image2D>();
  public static ArrayList<Image2D> special_alien_death = new ArrayList<Image2D>();
  public static ArrayList<Image2D> alien_death = new ArrayList<Image2D>();
  public static ArrayList<Image2D> rock = new ArrayList<Image2D>();
  public static ArrayList<Image2D> bullet_death = new ArrayList<Image2D>();
  public static ArrayList<Image2D> progressing_bar = new ArrayList<Image2D>();
  
  private static String ach_folder = "Achievements/";
  public static ArrayList<Image2D> achievements_images = new ArrayList<Image2D>();
  /* Animations && Achievements */
  
  /* Music */
  public static String audio_folder = "Audio/";
  public static ArrayList<Music> all_musics = new ArrayList<Music>();
  public static Music music_in_game = new Music(audio_folder + "music_in_game.wav");
  public static Music music_menu = new Music(audio_folder + "music_menu.wav");
  public static Music music_all_achievements = new Music(audio_folder + "music_all_achievements.wav");
  public static Music sound_alien_death = new Music(audio_folder + "alien_death.wav");
  public static Music sound_shoot = new Music(audio_folder + "shoot.wav");
  public static Music sound_special_alien = new Music(audio_folder + "special_alien.wav");
  public static Music sound_char_touched = new Music(audio_folder + "char_touched.wav");
  public static Music sound_achievement = new Music(audio_folder + "achievement.wav");
  public static Music sound_crash = new Music(audio_folder + "crash.wav");
  public static Music sound_ship_evolving = new Music(audio_folder + "ship_evolving.wav");
  public static Music sound_stanley = new Music(audio_folder + "stanley.wav");
  public static Music bonus = new Music(audio_folder + "bonus.wav");
  /* Music */
  
  public static void LoadContent()
  {
    SetFontWhite();
    
    for(int i = 0; i<=2; i++)
      menu_background.add(new Image2D(image_folder + anim_folder + "menu_background/" + i + ".png"));
    
    for(int i = 0; i<=8; i++)
    {
      ship_move.add(new Image2D(image_folder + anim_folder 
                                  + "ship_move/" + i + ".png"));
      goldenship_move.add(new Image2D(image_folder + anim_folder 
                                        + "goldenship_move/" + i + ".png"));
      ship_death.add(new Image2D(image_folder + anim_folder 
                                  + "ship_death/" + i + ".png"));
      goldenship_death.add(new Image2D(image_folder + anim_folder 
                                  + "goldenship_death/" + i + ".png"));
    }
    
    for(int i = 0; i<=3; i++)
    {
      ship_destructor_move.add(new Image2D(image_folder + anim_folder 
                                             + "ship_destructor_move/" + i + ".png"));
      goldenship_destructor_move.add(new Image2D(image_folder + anim_folder 
                                                   + "goldenship_destructor_move/" + i + ".png"));
    }
    
    for(int i = 0; i<=10; i++)
      ship_evolution.add(new Image2D(image_folder + anim_folder + "ship_evolution/" + i + ".png"));
    
    for(int i = 0; i<GameLoop.NB_ALIENS_C; i++)
    {
      alien_move.add(new ArrayList<Image2D>());
      
      for(int j = 0; j<=4; j++)
        alien_move.get(i).add(new Image2D(image_folder + anim_folder + "alien_move/" + i + "/" + j + ".png"));
    }
    
    for(int i = 0; i<=4; i++)
      alien_death.add(new Image2D(image_folder + anim_folder + "alien_death/" + i + ".png"));
    
    for(int i = 0; i<=1; i++)
      special_alien_move.add(new Image2D(image_folder + anim_folder + "special_alien_move/" + i + ".png"));
    
    for(int i = 0; i<=3; i++)
      special_alien_death.add(new Image2D(image_folder + anim_folder + "special_alien_death/" + i + ".png"));
    
    for(int i = 0; i<=2; i++)
      rock.add(new Image2D(image_folder + anim_folder + "rock/" + i + ".png"));
    
    for(int i = 0; i<=3; i++)
      bullet_death.add(new Image2D(image_folder + anim_folder + "bullet_death/" + i + ".png"));
    
    for(int i = 0; i<=5; i++)
      progressing_bar.add(new Image2D(image_folder + anim_folder + "progressing_bar/" + i + ".png"));
    
    for(int i = 0; i < Achievement.NB_ACHIEVEMENTS; i++)
      achievements_images.add(new Image2D(image_folder + ach_folder + i + ".png"));
    
    StdDraw.setFont(font);
    
    all_musics.add(music_in_game);
    all_musics.add(music_menu);
    all_musics.add(music_all_achievements);
    all_musics.add(sound_alien_death);
    all_musics.add(sound_shoot);
    all_musics.add(sound_special_alien);
    all_musics.add(sound_char_touched);
    all_musics.add(sound_achievement);
    all_musics.add(sound_crash);
    all_musics.add(sound_ship_evolving);
    all_musics.add(sound_stanley);
    all_musics.add(bonus);
  }
  
}