package model;

public class XY
{
    
    private int X;
    
    private int Y;
    
    public XY(){
        
    }
    
    public XY(String xy){
        String [] xys = xy.split(",");
        this.X = Integer.parseInt(xys[0]);
        this.Y = Integer.parseInt(xys[1]);
    }
    
    public XY(int x , int y){
        this.X = x ;
        this.Y = y ;
    }
    

    public int getX()
    {
        return X;
    }

    public void setX(int x)
    {
        X = x;
    }

    public int getY()
    {
        return Y;
    }

    public void setY(int y)
    {
        Y = y;
    }
    
    
    
}
