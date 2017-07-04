    package listener;  
      
    import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import controller.MainControl;
      
    public class Hello implements ActionListener {  
        private static String labelPrefix = "Number of button clicks: ";  
        private int numClicks = 0;  
        final JLabel label = new JLabel(labelPrefix + "0    ");  
      
        //Specify the look and feel to use.  Valid values:  
        //null (use the default), "Metal", "System", "Motif", "GTK+"  
        final static String LOOKANDFEEL = "System";  
      
        public Component createComponents() {  
            JButton button = new JButton("I'm a Swing button!");  
            button.setMnemonic(KeyEvent.VK_I);  
            button.addActionListener(this);  
            label.setLabelFor(button);  
      
            /* 
             * An easy way to put space between a top-level container 
             * and its contents is to put the contents in a JPanel 
             * that has an "empty" border. 
             */  
            JPanel pane = new JPanel(new GridLayout(0, 1));  
            pane.add(button);  
            pane.add(label);  
            pane.setBorder(BorderFactory.createEmptyBorder(  
                                            30, //top  
                                            30, //left  
                                            10, //bottom  
                                            30) //right  
                                            );  
      
            return pane;  
        }  
      
        public void actionPerformed(ActionEvent e) {  
            numClicks = MainControl.getCount();
            label.setText(labelPrefix + numClicks);  
            
            /**
             * 只要能点到，说明未在游戏中
             * 第一次点到时，初始值为true，此时说明游戏刚结束
             */
            if(MainControl.isGameStart()){
                MainControl.setGameOver(true);
            }
            MainControl.setGameStart(false);
            MainControl.setLastClick(System.currentTimeMillis());
        }  
      
        private static void initLookAndFeel() {  
            String lookAndFeel = null;  
      
            if (LOOKANDFEEL != null) {  
                if (LOOKANDFEEL.equals("Metal")) {  
                    lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();  
                } else if (LOOKANDFEEL.equals("System")) {  
                    lookAndFeel = UIManager.getSystemLookAndFeelClassName();  
                } else if (LOOKANDFEEL.equals("Motif")) {  
                    lookAndFeel = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";  
                } else if (LOOKANDFEEL.equals("GTK+")) { //new in 1.4.2  
                    lookAndFeel = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";  
                } else {  
                    System.err.println("Unexpected value of LOOKANDFEEL specified: "  
                                       + LOOKANDFEEL);  
                    lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();  
                }  
      
                try {  
                    UIManager.setLookAndFeel(lookAndFeel);  
                } catch (ClassNotFoundException e) {  
                    System.err.println("Couldn't find class for specified look and feel:"  
                                       + lookAndFeel);  
                    System.err.println("Did you include the L&F library in the class path?");  
                    System.err.println("Using the default look and feel.");  
                } catch (UnsupportedLookAndFeelException e) {  
                    System.err.println("Can't use the specified look and feel ("  
                                       + lookAndFeel  
                                       + ") on this platform.");  
                    System.err.println("Using the default look and feel.");  
                } catch (Exception e) {  
                    System.err.println("Couldn't get specified look and feel ("  
                                       + lookAndFeel  
                                       + "), for some reason.");  
                    System.err.println("Using the default look and feel.");  
                    e.printStackTrace();  
                }  
            }  
        }  
      
        /** 
         * Create the GUI and show it.  For thread safety, 
         * this method should be invoked from the 
         * event-dispatching thread. 
         */  
        public static void createAndShowGUI() {  
            //Set the look and feel.---设置外观，可以忽略  
            initLookAndFeel();  
      
            //Make sure we have nice window decorations.  
            //设置为false的话，即为不改变外观  
            JFrame.setDefaultLookAndFeelDecorated(true);  
      
            //Create and set up the window.  
            JFrame frame = new JFrame("stopcheck");  
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
            frame.setAlwaysOnTop(true);
      
            Hello app = new Hello();  
            Component contents = app.createComponents();  
            frame.getContentPane().add(contents, BorderLayout.CENTER);  
      
            //Display the window.  
            frame.pack();  
            frame.setVisible(true);  
        }  
      
//        public static void main(String[] args) {  
//            //Schedule a job for the event-dispatching thread:  
//            //creating and showing this application's GUI.  
//            
//        }  
    }  