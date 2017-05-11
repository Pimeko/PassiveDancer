import java.util.*;

public class CollisionManager
{
  // Checks the collisions between the balls and everything else, one after another
  public static void Collisions(Ship ship, LinkedList<Ball> balls, Alien[][] aliens, 
                                ArrayList<Bunker> bunkers, LinkedList<Bonus> bonus)
  {
    boolean stillAlive;
    for(Ball b : balls)
    {
      if(!b.IsBeingDestroyed())
      {
        stillAlive = true;
        
        /* < Char >*/
        if(!ship.justGotShot && 
           b.Rectangle().Intersects(ship.char_rectangle))
        {
          if(!ship.hasShield)
            ship.Touch();
          b.Destroy(true);
          stillAlive = false;
        }
        /* </ Char >*/
        
        if(stillAlive)
        {
          /* < Aliens > */
          int previous_nb_aliens_alien = GameLoop.nb_aliens_alive;
          for(int i = 0; stillAlive && i<GameLoop.NB_ALIENS_L;i++)
          {
            for(int j = 0; stillAlive && j<GameLoop.NB_ALIENS_C; j++)
            {
              Alien a = aliens[i][j];
              if(a.IsAlive())
              {
                if(a.rectangle.Intersects(b.Rectangle()) && b.direction == 1)
                {
                  a.Kill();
                  // Destructor Bonus
                  if(b.IsDestructor())
                  {
                    if(i>0)
                      aliens[i-1][j].Kill();
                    if(i<GameLoop.NB_ALIENS_L-1)
                      aliens[i+1][j].Kill();
                    if(b.FromShip() && PassiveDancer.activateFeatures && Achievement.isGolden)
                      ship.nb_kills_special_done++;
                  }
                  // All Achievement Bonus
                  else if(b.IsSpecial())
                  {
                    for(int i_2=0; i_2<GameLoop.NB_ALIENS_L; i_2++)
                      aliens[i_2][j].Kill();
                    for(int j_2=0; j_2<GameLoop.NB_ALIENS_C; j_2++)
                      aliens[i][j_2].Kill();
                    ship.nb_kills_special_done = 0;
                  }
                  else if(b.FromShip() && PassiveDancer.activateFeatures && Achievement.isGolden)
                    ship.nb_kills_special_done+=(previous_nb_aliens_alien - GameLoop.nb_aliens_alive);
                  
                  b.Destroy(true);
                  stillAlive = false;
                }
                // Intersection between the alien and the ship (already in the loop, better check now)
                else if(a.rectangle.Intersects(ship.char_rectangle))
                {
                  GameLoop.EndGame();
                  b.Destroy(true);
                  stillAlive = false;
                }
              }
            }
          }
          if(!stillAlive)
          {
            if(GameLoop.nb_aliens_alive == previous_nb_aliens_alien - 2)
              Achievement.SetAchieved(Achievement.AchievementType.DoubleShot);
            else if(GameLoop.nb_aliens_alive == previous_nb_aliens_alien - 3)
              Achievement.SetAchieved(Achievement.AchievementType.TripleShot);
          }
          
          /* < Special Alien >*/
          if(stillAlive && b.IsAlive() && SpecialAlien.oneAlive)
          {
            SpecialAlien a = GameLoop.special_alien;
            if(a.rectangle.Intersects(b.Rectangle()) && a.isAlive)
            {
              a.Kill(true);
              b.Destroy(true);
              stillAlive = false;
            }
          }
          /* </ Special Alien >*/
          
          /* </ Aliens >*/
          
          if(stillAlive)
          {
            /* < Rocks > */
            for(int n = 0; stillAlive && n<bunkers.size(); n++)
            {
              Bunker bunker = bunkers.get(n);
              {
                for(int i = 0; stillAlive && i< bunker.NB_ROCKS_L; i++)
                {
                  for(int j = 0; stillAlive && j< bunker.NB_ROCKS_C; j++)
                  {
                    Rock r = bunker.rocks[i][j];
                    if(r.isAlive && b.Rectangle().Intersects(r.rectangle))
                    {
                      bunker.DestroyRock(i,j);
                      if(!bunker.IsAlive() && b.FromShip())
                        ship.nbRocksDestroyed++;
                      b.Destroy(true);
                      stillAlive = false;
                    }
                  }
                }
              }
            }
            /* </ Rocks > */
            
            if(stillAlive)
            {
              /* < Balls > */
              
              for(Ball newBall : balls)
              {
                if(b!=newBall && b.Rectangle().Intersects(newBall.Rectangle()))
                {
                  if(b.FromShip() || newBall.FromShip())
                    Achievement.SetAchieved(Achievement.AchievementType.BulletWall);
                  b.Destroy(true);
                  newBall.Destroy(true);
                  stillAlive = false;
                }
              }
              /* </ Balls >*/
            }
          }
        }
      }
    }
    
    // Bonus and ship
    for(Bonus b : bonus)
    {
      if(b.rectangle.Intersects(ship.char_rectangle) && !b.IsActive())
      {
        b.Activate();
        ship.GiveBonus(b);
      }
    }
  }
}