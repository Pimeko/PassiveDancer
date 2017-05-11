public class BonusLife extends Bonus
{
  public BonusLife(double x, double y)
  {
    super(x, y);
    bonusType = Bonus.BonusType.Life;
    image = ContentManager.BonusTypeToImage(bonusType);
    rectangle = new Rectangle2D(x, y, 
                                Utilities.GetRatioW(image),
                                Utilities.GetRatioH(image));
    timer_active = 300;
  }
}