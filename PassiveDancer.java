import java.awt.event.KeyEvent;

public class PassiveDancer
{
  public enum GameState
  {
    Menu, GameLoop, Pause, Win, GameOver, AllAchievements, Quit;
  }
  public static GameState gameState;
  public static boolean activateFeatures, englishMode;
  
  public static void main(String[] args)
  {
    gameState = GameState.Menu;
    StdDraw.setCanvasSize(ContentManager.WindowWidth, ContentManager.WindowHeight);
    ContentManager.LoadContent();
    Achievement.Initialize();
    ExternalManager.Initialize();
    MusicManager.Initialize();
    MusicManager.ChangeMusic(MusicManager.Musics.Menu);
    Menu.Initialize();
    
    while(true)
    {
      StdDraw.clear(StdDraw.BLACK);
      
      if(StdDraw.isKeyPressed(KeyEvent.VK_Q) && gameState != GameState.Quit)
      {
        MenuQuit.Initialize(gameState);
        gameState = GameState.Quit;
      }
      
      switch(gameState)
      {
        case Menu:
          Menu.Update();
          MusicManager.Update();
          Menu.Draw();
          break;
        case GameLoop:
          GameLoop.Update();
          GameLoop.Draw(true);
          break;
        case Pause:
          PauseMenu.Update();
          GameLoop.Draw(false);
          PauseMenu.Draw();
          break;
        case GameOver:
          GameOverScreen.Update();
          MusicManager.Update();
          GameOverScreen.Draw();
          MusicManager.Draw();
          break;
        case AllAchievements:
          MenuAllAchievements.Update();
          MenuAllAchievements.Draw();
          break;
        case Quit:
          MenuQuit.Update();
          MenuQuit.Draw();
          break;
        default:
          Menu.Update();
          MusicManager.Update();
          Menu.Draw();
          break;
      }
      ExternalManager.Draw();
      
      StdDraw.show(10);
    }
  }
}