package controller;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Properties;

import listener.Hello;
import model.XY;
import utils.FileUtil;

/**
 * 准备：关闭其他所有可能出现弹窗的窗口，手动启动游戏 进入开始页面，开启软件 1.第一次进入游戏鼠标挪动到“开始游戏” 2.左键单击“人机”
 * 3.第一次之后，每次点击“再来一次” 3.鼠标移动“开始匹配” 4.左键单击 5.鼠标移动到“确认” 6.每3s单击一次，1分钟后默认开始选人
 * 7.鼠标移动到“寒冰”位置 8.左键单击 9.移动到“锁定”位置 10.左键单击 11.检测是否进入游戏，若是进入游戏则开始游戏步骤
 * 12.另外启动线程10分钟后开始每1s检测是否结束游戏 13.检测出游戏结束后开始递归
 * 
 * 游戏中步骤： p打开装备栏-装备位置-点击选择-购买-关闭装备栏-下路草丛-不补兵（5分钟后回家打野刀，之后开始打野）
 * 
 * 打野路线： 上半部分：小青蛙位置-30s后移动到蓝-1分钟s后移动到3狼-1分钟s后移动到4f-1分钟后移动到红-1分钟s后移动到石头人一分钟后回城，递归
 * 
 * 全程计时，20分钟后每两分钟发起投降
 * 
 * 
 */
public class MainControl
{
    private static int count = 0;

    private static Properties properties;

    private static boolean gameStart = false;

    private static boolean gameOver = true;

    private static long lastClick = 0l;

    /**
     * InputEvent.BUTTON1_MASK 左键 InputEvent.BUTTON2_MASK 中键
     * InputEvent.BUTTON3_MASK 右键
     */
    private static int left;

    private static int right;

    private static Robot robot;

    /**
     * @Title: main
     * @Description: TODO(中心控制类)
     * @author Dangzhang
     * @param args
     * @throws
     */
    public static void main(String[] args)
    {
        // 加载配置文件
        properties = FileUtil.getProperties("config.properties");
        left = InputEvent.BUTTON1_MASK;
        right = InputEvent.BUTTON3_MASK;
        try
        {
            robot = new Robot();
        }
        catch (AWTException e1)
        {
            e1.printStackTrace();
        }
        try
        {
            // 启动检测窗口
            javax.swing.SwingUtilities.invokeLater(new Runnable()
            {
                public void run()
                {
                    Hello.createAndShowGUI();
                }
            });

            System.out.println("检测窗口已经启动");
            // 开始自动控制
            startGame(properties);

        }
        catch (AWTException e)
        {
            e.printStackTrace();
        }

    }

    public static void startGame(Properties properties) throws AWTException
    {
        Thread gameOver = new Thread()
        {
            public void run()
            {
                robot.delay(15 * 60 * 1000);
                while (true)
                {
                    MainControl.mouseMoveClick(robot, "gameover",
                            MainControl.getLeft());
                    robot.delay(1000);
                }

            };
        };
        // 游戏主线程
        Thread mainThread = new Thread(new mainRunable());

        // 监听线程
        Thread listener = new Thread()
        {
            public void run()
            {
                while (true)
                {
                    MainControl.mouseMoveClick(robot, "checkGame",
                            MainControl.getLeft());
                    // 若是大于1.5秒未更新，说明已经进入游戏，正常更新时间应该为1秒一次
                    if (System.currentTimeMillis() - MainControl.getLastClick() > 1500)
                    {
                        if (!MainControl.isGameStart())
                        {
                            // 说明已经选择完角色开始进入加载
                            MainControl.setGameOver(false);
                            System.out.println("已经选择完角色开始进入加载");
                        }
                        MainControl.setGameStart(true);
                    }
                    try
                    {
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }

            }
        };

        listener.start();

        // 控制主线程开关
        boolean last = false;
        while (true)
        {
            // 第一次，启动游戏
            if (MainControl.getCount() == 0)
            {
                mainThread.start();
                gameOver.start();
                MainControl.setCount(MainControl.getCount() + 1);
            }
            else
            {

                if (!last)
                {
                    if (MainControl.isGameOver())
                    {// 说明游戏刚结束
                        reStartThread(mainThread);
                        reStartThread(gameOver);
                        System.out.println("下次游戏成功开始");
                    }
                }
                else
                {
                    if (!MainControl.isGameOver())
                    {// 说明游戏刚开始
                        System.out.println("游戏开始加载");
                    }
                }
            }
            last = MainControl.isGameOver();
            if (last)
            {

            }
        }

    }

    public static void reStartThread(Thread th)
    {
        try
        {
            th.interrupt();
            if (th.isAlive())
            {
                reStartThread(th);
            }
            else
            {
                th.start();
                MainControl.setCount(MainControl.getCount() + 1);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public static void daye(Robot robot)
    {
        int n = 1;
        while (n < 7)
        {
            mouseMoveClick(robot, "xy" + n, right);

            for (int a = 0; a < 100; a++)
            {
                robot.delay(getProTimeByname("xy" + n + "ss") / 100);
                // 攻击指令
                robot.keyPress(KeyEvent.VK_A);
                robot.keyRelease(KeyEvent.VK_A);
            }

            n++;
        }
        // 打完一轮回城
        robot.keyPress(KeyEvent.VK_B);
        robot.keyRelease(KeyEvent.VK_B);

        robot.delay(15 * 1000);

        daye(robot);
    }

    /**
     * 
     * @Title: mouseMoveClick
     * @Description: TODO(移动鼠标到配置文件中对应名称点，并根据对应按键点击)
     * @author winterfing
     * @param robot
     * @param XYName
     * @param clickType
     * @throws
     */
    public synchronized static void mouseMoveClick(Robot robot, String XYName,
            int clickType)
    {
        // 移动到“开始位置”
        XY onceStart = new XY(properties.getProperty(XYName));
        robot.mouseMove(onceStart.getX(), onceStart.getY());

        // 左键单击
        robot.mousePress(clickType);// 按下
        robot.mouseRelease(clickType);// 释放
    }

    /* get and set */

    public static int getProTimeByname(String name)
    {
        return Integer.parseInt(properties.getProperty(name));
    }

    public static Robot getRobot()
    {
        return robot;
    }

    public static void setRobot(Robot robot)
    {
        MainControl.robot = robot;
    }

    public static int getCount()
    {
        return count;
    }

    public static void setCount(int count)
    {
        MainControl.count = count;
    }

    public static Properties getProperties()
    {
        return properties;
    }

    public static void setProperties(Properties properties)
    {
        MainControl.properties = properties;
    }

    public static boolean isGameStart()
    {
        return gameStart;
    }

    public static void setGameStart(boolean gameStart)
    {
        MainControl.gameStart = gameStart;
    }

    public static boolean isGameOver()
    {
        return gameOver;
    }

    public static void setGameOver(boolean gameOver)
    {
        MainControl.gameOver = gameOver;
    }

    public static int getLeft()
    {
        return left;
    }

    public static void setLeft(int left)
    {
        MainControl.left = left;
    }

    public static int getRight()
    {
        return right;
    }

    public static void setRight(int right)
    {
        MainControl.right = right;
    }

    public static long getLastClick()
    {
        return lastClick;
    }

    public static void setLastClick(long lastClick)
    {
        MainControl.lastClick = lastClick;
    }

}

class mainRunable implements Runnable
{
    public void run()
    {
        Robot robot = MainControl.getRobot();
        // 第一次开启
        if (MainControl.getCount() == 0)
        {
            System.out.println("第一次游戏开始");
            MainControl.mouseMoveClick(robot, "onceStart",
                    MainControl.getLeft());
        }
        else
        {
            System.out.println("游戏开始");
            MainControl.mouseMoveClick(robot, "agin", MainControl.getLeft());
        }
        // 开始匹配pipei
        MainControl.mouseMoveClick(robot, "pipei", MainControl.getLeft());
        // 等待设定时间后点击确定
        robot.delay(MainControl.getProTimeByname("pipeitime"));
        MainControl.mouseMoveClick(robot, "pipeiOk", MainControl.getLeft());
        // 过几秒再选
        robot.delay(MainControl.getProTimeByname("gjmzx"));
        // 选择人物并确定
        MainControl.mouseMoveClick(robot, "rwweizhi", MainControl.getLeft());
        MainControl.mouseMoveClick(robot, "xuanding", MainControl.getLeft());
        // 检测是否进入游戏
        while (!MainControl.isGameStart())
        {
            System.out.println("等待进入游戏");
            robot.delay(1000);
        }
        // 进入加载页面后多久算开始游戏
        robot.delay(MainControl.getProTimeByname("howstart"));

        // 开始游戏
        // 打开商品
        robot.keyPress(KeyEvent.VK_P);
        robot.keyRelease(KeyEvent.VK_P);

        // 购买初始装备
        MainControl.mouseMoveClick(robot, "cszbwz", MainControl.getLeft());
        robot.delay(1000);
        MainControl.mouseMoveClick(robot, "gmanwz", MainControl.getLeft());
        robot.delay(1000);
        robot.keyPress(KeyEvent.VK_P);
        robot.keyRelease(KeyEvent.VK_P);

        robot.delay(1000);

        // 移动到下路
        MainControl.mouseMoveClick(robot, "caocong", MainControl.getRight());

        robot.delay(30 * 1000);
        robot.delay(MainControl.getProTimeByname("caocongs"));

        robot.keyPress(KeyEvent.VK_B);
        robot.keyRelease(KeyEvent.VK_B);

        robot.delay(10 * 1000);

        robot.keyPress(KeyEvent.VK_P);
        robot.keyRelease(KeyEvent.VK_P);
        robot.delay(1000);
        MainControl.mouseMoveClick(robot, "dychjzb", MainControl.getLeft());
        robot.delay(1000);
        MainControl.mouseMoveClick(robot, "gmanwz", MainControl.getLeft());
        robot.keyPress(KeyEvent.VK_P);
        robot.keyRelease(KeyEvent.VK_P);

        MainControl.daye(robot);

    }
}
