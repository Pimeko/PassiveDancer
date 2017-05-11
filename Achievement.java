import java.util.*;

public class Achievement
{
  public enum AchievementType
  {
    Sniper(0), 
      DoubleShot(1), 
      TripleShot(2), 
      UFO(3), 
      SelfSabotage(4), 
      BonusNeeded(5), 
      WhoNeedsBonus(6),
      Perfect(7),
      BulletWall(8),
      HighestGround(9),
      PGM(10);
    
    private final int value;
    private AchievementType(int value) { this.value = value; }
    public int getValue() { return value; }
  }
  public static final int NB_ACHIEVEMENTS = AchievementType.values().length;
  public static int nb_achievements_done, nb_achievements_previously_done;
  public static boolean isDisplaying, isGolden, keepsOnDisplaying;
  private static Queue<AchievementType> achievements_appearance;
  private static AchievementType current_type;
  private static double timer_appearance, timer_appearance_waited;
  private static double x, y, x_ini, y_ini;
  public static boolean[] achievements_done;
  
  public static void Initialize()
  {
    achievements_done = new boolean[NB_ACHIEVEMENTS];
    isDisplaying = false;
    achievements_appearance = new LinkedList<AchievementType>();
    
    timer_appearance = 150;
    timer_appearance_waited = 0;
    x_ini = 0.5;
    y_ini = 1.2;
    
    CheckAllAchievements();
  }
  
  public static void Reset()
  {    
    isDisplaying = false;
    achievements_appearance.clear();
    timer_appearance_waited = 0;
  }
  
  private static void SetAchievementDone(AchievementType type)
  {
    achievements_done[type.getValue()] = true;
  }
  
  public static boolean GetAchievementDone(AchievementType type)
  {
    return achievements_done[type.getValue()];
  }
  
  private static void RefreshNbAchievementsDone()
  {
    nb_achievements_done = 0;
    for(int i = 0; i < NB_ACHIEVEMENTS; i++)
      if(achievements_done[i]) { nb_achievements_done ++; } 
  }
  
  public static void SetAchieved(AchievementType currentType)
  {
    if((PassiveDancer.activateFeatures) && !GetAchievementDone(currentType))
    {
      Display(currentType);
      SetAchievementDone(currentType);
      RefreshNbAchievementsDone();
      ExternalManager.SaveAchievements();
    }
  }
  
  public static boolean CheckAllAchievements()
  {
    isGolden = (nb_achievements_done == NB_ACHIEVEMENTS);
    return isGolden;
  }
  
  // Adds an achievement to the queue of display
  public static void Display(AchievementType type)
  {
    achievements_appearance.add(type);
    ContentManager.sound_achievement.Play(false);
  }
  
  public static boolean IsDisplaying()
  {
    return (!achievements_appearance.isEmpty());
  }
  
  // Parses the queue of achievements and display them one by one until it's empty
  public static void Draw()
  {
    if(isDisplaying)
    {
      keepsOnDisplaying = true;
      if(timer_appearance_waited >= timer_appearance)
      {
        if(y<y_ini)
        {
          y+=0.01;
          Image2D img = ContentManager.achievements_images.get(current_type.getValue());
          StdDraw.picture(x,y,img.path);
        }
        else
        {
          timer_appearance_waited = 0;
          isDisplaying = false;
        }
      }
      else
      {
        timer_appearance_waited++;
        Image2D img = ContentManager.achievements_images.get(current_type.getValue());
        StdDraw.picture(x,y,img.path);
        if(y>1)
          y-=0.01;
      }
    }
    else
    {
      if(!achievements_appearance.isEmpty())
      {
        current_type = achievements_appearance.poll();
        isDisplaying = true;
        x=x_ini;
        y=y_ini;
      }
      else
        keepsOnDisplaying = false;
    }
  }
}