package model;

import java.io.Serializable;

public class Role implements Serializable
{

    /**
    * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
    */
    private static final long serialVersionUID = -4935408337811905947L;
    
    private String roleName;
    
    private int stepLength;
    
    private XY defaultXY;
    
    
    public Role()
    {
        defaultXY = new XY(0 , 0);
    }
    
    public Role(int x , int y)
    {
        defaultXY = new XY(x , y);
    }
    

    public String getRoleName()
    {
        return roleName;
    }

    public void setRoleName(String roleName)
    {
        this.roleName = roleName;
    }

    public int getStepLength()
    {
        return stepLength;
    }

    public void setStepLength(int stepLength)
    {
        this.stepLength = stepLength;
    }

    public XY getDefaultXY()
    {
        return defaultXY;
    }

    public void setDefaultXY(XY defaultXY)
    {
        this.defaultXY = defaultXY;
    }
    
    
    
    
}
