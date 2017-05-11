import java.util.*;

public class Bunker
{
  double x, y; // Position top left
  public Rock[][] rocks;
  public final int NB_ROCKS_L = 5, NB_ROCKS_C = 3;
  public int nb_rocks_left;
  boolean isAlive;
  public boolean IsAlive() { return isAlive; }
  
  public enum BunkerType
  {
    Square, Stairs;
  }
  BunkerType currentType;
  
  public Bunker(double x, double y, BunkerType currentType)
  {
    this.x = x;
    this.y = y;
    
    rocks = new Rock[NB_ROCKS_L][NB_ROCKS_C]; // [Column][Line]
    FillArray(rocks, currentType);
    isAlive = true;
  }
  
  public void Reset()
  {
    isAlive = true;
    rocks = new Rock[NB_ROCKS_L][NB_ROCKS_C];
    FillArray(rocks, currentType);
  }
  
  private void FillArray(Rock[][] array, BunkerType t)
  {
    double rock_width = ContentManager.rock.get(0).Width();
    double rock_height = ContentManager.rock.get(0).Height();
    
    switch(t)
    {
      // Fills a whole square
      case Square:
      {
        for(int i = 0; i<NB_ROCKS_L; i++)
        {
          for(int j = 0; j<NB_ROCKS_C; j++)
          {
            double newX = x + Utilities.GetRatioW(i*rock_width);
            double newY = y + Utilities.GetRatioH(j*rock_height);
            Rock rock = new Rock(newX, newY);
            rock.Initialize();
            array[i][j] = rock;
            nb_rocks_left++;
          }
        }
      }
      break;
      
      /*   X
       *  XXX
       * XXXXX */
      case Stairs:
      {
        for(int i = 0; i<NB_ROCKS_L; i++)
        {
          for(int j = 0; j<NB_ROCKS_C; j++)
          {
            if((j==NB_ROCKS_C-1) || 
               (j==NB_ROCKS_C-2 && i > 0 && i < NB_ROCKS_L -1) || 
               (j == NB_ROCKS_C-3 && i==NB_ROCKS_L/2))
            {
              double newX = x + Utilities.GetRatioW(i*rock_width);
              double newY = y + Utilities.GetRatioH(j*rock_height);
              Rock rock = new Rock(newX, newY);
              rock.Initialize();
              array[i][j] = rock;
              nb_rocks_left++;
            }
            else
            {
              array[i][j] = new Rock();
            }
          }
        }
      }
        break;
        
      default:
        break;
    }
  }
  
  public void Draw()
  {
    if(isAlive)
    {
      for(int i = 0; i<NB_ROCKS_L; i++)
        for(int j = 0; j<NB_ROCKS_C; j++)
        rocks[i][j].Draw();
    }
  }
  
  // Reduces the state or destroys the rock
  public void DestroyRock(int i, int j)
  {
    Rock rock = rocks[i][j];
    rock.Destroy();
    if(!rock.isAlive)
      nb_rocks_left--;
    if(nb_rocks_left == 0)
    {
      isAlive = false;
    }
  }
}