public class BonusShield extends Bonus
{
  public BonusShield(double x, double y)
  {
    super(x, y);
    bonusType = Bonus.BonusType.Shield;
    image = ContentManager.BonusTypeToImage(bonusType);
    rectangle = new Rectangle2D(x, y, 
                                Utilities.GetRatioW(image),
                                Utilities.GetRatioH(image));
    timer_active = 400;
  }
}