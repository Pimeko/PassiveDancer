public class BonusSlow extends Bonus
{
  public final double newSpeed = 0.001;
  public BonusSlow(double x, double y)
  {
    super(x, y);
    bonusType = Bonus.BonusType.Slow;
    image = ContentManager.BonusTypeToImage(bonusType);
    rectangle = new Rectangle2D(x, y, 
                                Utilities.GetRatioW(image),
                                Utilities.GetRatioH(image));
    timer_active = 500;
  }
}