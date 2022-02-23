package runner;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import mechanics.BattleWindow;
import mechanics.HelpWindow;

/**
 * File: src/runner/Main.java
 * <P>
 * The main menu window. Contains a {@code main} method that
 * runs the entire program.
 * 
 * @author Samuel Tan
 *
 */
public class Main extends JFrame implements ActionListener
{
  private JPanel mainMenu;
  private BattleWindow battleWindow;
  private HelpWindow help;
  private JButton sandboxButton;
  private JButton campaignButton, quitButton, helpButton;
  
  public Main()
  {
    super("March of the Little Boxes");
    
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setResizable(false);
    
    Image image;
    try
    {
//      img = ImageIO.read(new java.io.File("title3.png"));
      image = ImageIO.read(getClass().getResource("/title3.png"));
//      img = getToolkit().getImage((getClass().getResource("/resources/title3.png")));
//      img = java.awt.Toolkit.getDefaultToolkit().getImage(
//          Thread.currentThread().getContextClassLoader().getResource("/resources/title3.png"));
    } 
    catch (Exception e1)
    {
//      e1.printStackTrace();
      try
      {
        image = ImageIO.read(new File("title3.png"));
      }
      catch (Exception e2)
      {
        e2.printStackTrace();
        image = createImage(600, 450);
      }
    }
    
    final Image finalImage = image;
    
    mainMenu = new JPanel(new BorderLayout())
    {
      @Override
      public void paintComponent(Graphics g)
      { 
        g.drawImage(finalImage, 0, 0, null);

        ((Graphics2D) g).setRenderingHints(new RenderingHints(
            RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON));

        g.setFont(new Font(Font.DIALOG, Font.BOLD, 36));
        g.setColor(Color.GRAY);
        g.drawString("March of the Little Boxes", 85, 50);
      }
    };
    
    mainMenu.setPreferredSize(new Dimension(600, 450));
    mainMenu.setBackground(Color.WHITE);
    
    JPanel buttons = new JPanel();
    buttons.setOpaque(false);
    
    campaignButton = new JButton("Play Campaign");
    campaignButton.addActionListener(this);
    buttons.add(campaignButton);
    
    sandboxButton = new JButton("Enter Sandbox");
    sandboxButton.addActionListener(this);
    buttons.add(sandboxButton);
    
    helpButton = new JButton("Help");
    helpButton.addActionListener(this);
    buttons.add(helpButton);
    
    quitButton = new JButton("Quit");
    quitButton.addActionListener(this);
    buttons.add(quitButton);
    
    mainMenu.add(buttons, BorderLayout.SOUTH);
    
    setContentPane(mainMenu);
    pack();
    setLocationRelativeTo(null);
  }

  public void actionPerformed(ActionEvent e)
  {
    if (e.getSource() == sandboxButton)
    {
      this.setVisible(false);
      battleWindow = new BattleWindow(this, "Sandbox");
      battleWindow.setVisible(true);
    }
    else if (e.getSource() == campaignButton)
    {
      this.setVisible(false);
      battleWindow = new BattleWindow(this, "Campaign");
      battleWindow.setVisible(true);
    }
    else if (e.getSource() == helpButton)
    {
//      this.setVisible(false);
      if (help == null)
      {
        help = new HelpWindow(this);
        help.setVisible(true);
      }
    }
    else if (e.getSource() == quitButton)
    {
      System.exit(0);
    }
  }
  
  public void clearHelp()
  {
    help = null;
  }

  public static void main(String[] args)
  {
    try
    {
      UIManager.setLookAndFeel(
        UIManager.getSystemLookAndFeelClassName());
    }
    catch (Exception e) 
    {
      e.printStackTrace();
    }
    
    Main main = new Main();
    main.setVisible(true);
  }
}
