import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;

/* This class groups all the interactions with external files */
public class ExternalManager
{
  private static String score_file_name = "BestScore.txt";
  public static int currentBestScore, timer_saving, timer_saving_waited, angle_save;
  private static boolean isSaving = false;
  
  public static void Initialize()
  {
    timer_saving = 50;
    StopSaveAnimation();
    
    ReadBestScore();
    ReadAchievements();
    LoadPreferences();
  }
  
  // Returns the best score
  public static int ReadBestScore()
  {
    int best_score = -1;
    try 
    {
      BufferedReader file = new BufferedReader(new FileReader(score_file_name));
      best_score = Integer.parseInt(file.readLine());
      file.close();
    } 
    catch (Exception e) { SaveBestScore(0); }
    
    currentBestScore = best_score;
    return best_score;
  }
  
  // Replaces the best score
  public static void SaveBestScore(int new_score)
  {
    try
    {
      StartSaveAnimation();
      PrintWriter writer = new PrintWriter(score_file_name, "UTF-8");
      writer.print(new_score);
      writer.close();
      currentBestScore = new_score;
    }
    catch(Exception e) { System.out.println("Problem writing in the file " + score_file_name + "."); }
  }
  
  /* *********** */
  
  private static String achievements_file_name = "Achievements.txt";
  
  // Set all the previsoulsy done achievements
  public static void ReadAchievements()
  {
    try
    {
      BufferedReader file = new BufferedReader(new FileReader(achievements_file_name));
      int actualLine = 0, nb_achievements_done = 0;
      while(actualLine<Achievement.NB_ACHIEVEMENTS)
      {
        boolean currentValue = Utilities.IntToBool(Integer.parseInt(file.readLine()));
        Achievement.achievements_done[actualLine] = currentValue;
        if(currentValue)
          nb_achievements_done++;
        actualLine++;
      }
      file.close();
      Achievement.nb_achievements_done = nb_achievements_done;
      Achievement.CheckAllAchievements();
    } 
    catch (Exception e) { SaveAchievements(); }
  }
  
  // Replaces the values of all achievements
  public static void SaveAchievements()
  {
    try
    {
      StartSaveAnimation();
      PrintWriter writer = new PrintWriter(achievements_file_name, "UTF-8");
      int actualLine = 0, nb_achievements_done = 0;
      while(actualLine<Achievement.NB_ACHIEVEMENTS)
      {
        boolean currentValue = Achievement.achievements_done[actualLine];
        if(currentValue)
          nb_achievements_done++;
        writer.print(Utilities.BoolToInt(currentValue) + "\r\n");
        actualLine++;
      }
      writer.close();
      Achievement.nb_achievements_done = nb_achievements_done;
    }
    catch(Exception e) { System.out.println("Problem writing in the file " + achievements_file_name + "."); }
  }
  
  public static void SetAllAchievementsDone()
  {
    try
    {
      PrintWriter writer = new PrintWriter(achievements_file_name, "UTF-8");
      int actualLine = 0, nb_achievements_done = 0;
      while(actualLine<Achievement.NB_ACHIEVEMENTS)
      {
        Achievement.achievements_done[actualLine] = true;
        writer.print("1\r\n");
        actualLine++;
      }
      writer.close();
      Achievement.nb_achievements_done = Achievement.NB_ACHIEVEMENTS;
    }
    catch(Exception e) { System.out.println("Problem writing in the file " + achievements_file_name + "."); }
  }
  
  public static void ResetAchievements()
  {
    try
    {
      PrintWriter writer = new PrintWriter(achievements_file_name, "UTF-8");
      int actualLine = 0, nb_achievements_done = 0;
      while(actualLine<Achievement.NB_ACHIEVEMENTS)
      {
        writer.print(Utilities.BoolToInt(false) + "\r\n");
        actualLine++;
      }
      writer.close();
      Achievement.nb_achievements_done = nb_achievements_done;
      Achievement.CheckAllAchievements();
    }
    catch(Exception e) { System.out.println("Problem writing in the file " + achievements_file_name + "."); }
  }
  
  /* *********** */
  
  private static String preferences_file_name = "Preferences.txt";
  
  /* Music Active (0 : NO, 1 : YES)
   * Keyboard     (0 : EN, 1 : FR) */
  public static void SavePreferences()
  {
    try
    {
      StartSaveAnimation();
      PrintWriter writer = new PrintWriter(preferences_file_name, "UTF-8");
      
      writer.print(Utilities.BoolToInt(MusicManager.enable_music) + "\r\n");
      writer.print(Utilities.BoolToInt(PassiveDancer.englishMode) + "\r\n");
      
      writer.close();
    }
    catch(Exception e) { System.out.println("Problem writing in the file " + preferences_file_name + "."); }
  }
  
  public static void LoadPreferences()
  {
    try
    {
      BufferedReader file = new BufferedReader(new FileReader(preferences_file_name));
      
      MusicManager.enable_music = Utilities.IntToBool(Integer.parseInt(file.readLine()));
      PassiveDancer.englishMode = Utilities.IntToBool(Integer.parseInt(file.readLine()));
      
      file.close();
    } 
    catch (Exception e) { SavePreferences(); }
  }
  
  private static void StartSaveAnimation()
  {
    isSaving = true;
    timer_saving_waited = 0;
  }
  
  private static void StopSaveAnimation()
  {
    isSaving = false;
    timer_saving_waited = 0;
    angle_save = 0;
  }
  
  public static void Draw()
  {
    if(isSaving)
    {
      if(timer_saving_waited >= timer_saving)
        StopSaveAnimation();
      else
      {
        angle_save--;
        timer_saving_waited++;
        StdDraw.picture(0.95, 0.04, ContentManager.saving.path, angle_save);
      }
    }
  }
}