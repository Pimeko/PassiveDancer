import java.awt.event.KeyEvent;

public class MenuQuit
{
  private static PassiveDancer.GameState previousState;
  
  public static void Initialize(PassiveDancer.GameState _previousState)
  {
    previousState = _previousState;
  }
  
  public static void Update()
  {
    if(StdDraw.isKeyPressed(KeyEvent.VK_Y))
    {
      ExternalManager.SavePreferences();
      System.exit(0);
    }
    else if(StdDraw.isKeyPressed(KeyEvent.VK_N))
      PassiveDancer.gameState = previousState;
  }
  
  public static void Draw()
  {
    switch(previousState)
    {
      case Menu:
        Menu.Draw();
        break;
      case GameLoop:
        GameLoop.Draw(false);
        break;
      case Pause:
        GameLoop.Draw(false);
        PauseMenu.Draw();
        break;
      case GameOver:
        GameOverScreen.Draw();
        break;
      case AllAchievements:
        MenuAllAchievements.Draw();
        break;
      default:
        break;
    }
    StdDraw.picture(0.5, 0.5, ContentManager.fog.path, 1, 1);
    ContentManager.SetFontWhite();
    StdDraw.text(0.5, 0.5, "ARE YOU SURE YOU WANT TO QUIT ? Y/N");
  }
}