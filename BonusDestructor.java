public class BonusDestructor extends Bonus
{
  public BonusDestructor(double x, double y)
  {
    super(x, y);
    bonusType = Bonus.BonusType.Destructor;
    image = ContentManager.BonusTypeToImage(bonusType);
    rectangle = new Rectangle2D(x, y, 
                                Utilities.GetRatioW(image),
                                Utilities.GetRatioH(image));
    timer_active = 300;
  }
}