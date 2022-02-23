package mechanics;

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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import runner.Main;

/**
 * File: src/mechanics/HelpWindow.java
 * <P>
 * An explanatory JDialog that pops up when you click 
 * the "Help" button in the main menu.
 * 
 * @author Samuel Tan
 *
 */
public class HelpWindow extends JDialog
{
  public HelpWindow(Main main)
  {
    super(main, "March of the Little Boxes: Help");
    
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() 
    {
      public void windowClosing(WindowEvent e) 
      {
        main.clearHelp();
        dispose();
      }
    });
    setResizable(false);
    
    int width = 520, height = 390;

    Image image;
    try
    {
//      img = ImageIO.read(new File("title4.png"));
      image = ImageIO.read(getClass().getResource("/title4.png"));
    } 
    catch (Exception e1)
    {
//      e1.printStackTrace();
      try
      {
        image = ImageIO.read(new File("title4.png"));
      }
      catch (Exception e2)
      {
        e2.printStackTrace();
        image = createImage(width+60, height+30);
      }
    }

    final Image img = image;
    
    JPanel pane = new JPanel(new BorderLayout())
    {
      @Override
      public void paintComponent(Graphics g)
      { 
        int offset = 15;
        
        g.drawImage(img, -60, -30, null);

        ((Graphics2D) g).setRenderingHints(new RenderingHints(
            RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON));

        g.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
        g.setColor(Color.GRAY);
        g.drawString("How to play", offset, 30);
        g.setFont(new Font(Font.DIALOG, Font.PLAIN, 12));
//        g.setColor(Color.BLACK);
        g.drawString("Made by Samuel Tan", width - 120, height - 4);

        g.setColor(Color.DARK_GRAY);
        g.setFont(new Font(Font.DIALOG, Font.PLAIN, 16));
        g.drawString("This is a game about little boxes, \"units,\""
            + " that fight each other in teams.", offset, 70);
        
        g.drawString("The battle will be on the right, while your control panel"
            + " is on the left.", offset, 110);
        g.drawString("Select a unit type in your control panel, and"
            + " click somewhere on the", offset, 130);
        g.drawString("battle to place it there. (You can do this at any time.)"
            + " Or use the line tool!", offset, 150);
        g.drawString("You'll have to figure it out.", offset, 170);
        
        g.drawString("Each unit type behaves differently. It's your job to"
            + " study them and learn", offset, 210);
        g.drawString("how best to use them. Investigate the controls, too.", offset, 230);
        
        g.drawString("In campaign mode, you play as Blue Team with"
            + " limited resources.", offset, 270);
        g.drawString("Or, in sandbox mode, make as much carnage as you want!", offset, 290);
        
        g.drawString("The world is yours to conquer. Good luck, and have fun!", offset, 330);
      }
    };

    pane.setPreferredSize(new Dimension(width, height));
    pane.setBackground(Color.WHITE);

    JPanel bottom = new JPanel();
    JButton back = new JButton("Back to main menu");
    back.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e) 
      {
        main.clearHelp();
        dispose();
//        main.setVisible(true);
      }
    });
    
    bottom.add(back);
    bottom.setOpaque(false);
    pane.add(bottom, BorderLayout.SOUTH);

    setContentPane(pane);
    pack();
    setLocationRelativeTo(main);
  }
}
