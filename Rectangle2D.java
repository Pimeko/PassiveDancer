public class Rectangle2D
{
  // Position of the center
  public double x, y, width, height;
  
  public Rectangle2D(double x, double y, double width, double height)
  {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }
  
  public boolean Intersects(Rectangle2D rec)
  {
    return (x-width/2<rec.x + rec.width/2 &&
            x-width/2 + width > rec.x-rec.width/2 &&
            y-height/2 < rec.y + rec.height/2 &&
            height + y > rec.y - rec.height/2);
  }
  
  public void UpdatePos(double x, double y)
  {
    this.x = x;
    this.y = y;
  }
  
  public void Resize(double width, double height)
  {
    this.width = width;
    this.height = height;
  }
  
  public void Draw()
  {
    StdDraw.setPenColor(StdDraw.ORANGE);
    StdDraw.rectangle(x, y, width/2, height/2);
  }
}