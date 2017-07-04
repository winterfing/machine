package model;

public class DeskMap
{
    
    //初始点
    private XY startXY;
    
    //最大点
    private XY endXy;
    
    //第一次开始点
    private XY onceStart;
    
    //开始时匹配
    private XY pipei;
    
    //匹配确定
    private XY pipeiOk;
    
    //人物位置
    private XY rwweizhi;
    
    //选定按钮位置
    private XY xuanding;
    
    //点击确认是否进入游戏弹窗位置
    private XY checkGame;
    
    

    public XY getStartXY()
    {
        return startXY;
    }

    public void setStartXY(XY startXY)
    {
        this.startXY = startXY;
    }

    public XY getEndXy()
    {
        return endXy;
    }

    public void setEndXy(XY endXy)
    {
        this.endXy = endXy;
    }

    public XY getOnceStart()
    {
        return onceStart;
    }

    public void setOnceStart(XY onceStart)
    {
        this.onceStart = onceStart;
    }

    public XY getPipei()
    {
        return pipei;
    }

    public void setPipei(XY pipei)
    {
        this.pipei = pipei;
    }

    public XY getPipeiOk()
    {
        return pipeiOk;
    }

    public void setPipeiOk(XY pipeiOk)
    {
        this.pipeiOk = pipeiOk;
    }

    public XY getRwweizhi()
    {
        return rwweizhi;
    }

    public void setRwweizhi(XY rwweizhi)
    {
        this.rwweizhi = rwweizhi;
    }

    public XY getXuanding()
    {
        return xuanding;
    }

    public void setXuanding(XY xuanding)
    {
        this.xuanding = xuanding;
    }

    public XY getCheckGame()
    {
        return checkGame;
    }

    public void setCheckGame(XY checkGame)
    {
        this.checkGame = checkGame;
    }
    
}
