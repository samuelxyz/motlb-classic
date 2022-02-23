package mechanics;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import runner.Main;

/**
 * File: src/mechanics/BattleWindow.java
 * <P>
 * A window with a {@code ControlPanel} on the left and
 * a {@code battle} on the right. This is used for both sandbox
 * and campaign mode.
 * 
 * @author Samuel Tan
 *
 */
public class BattleWindow extends JFrame
{
  Battle battle;
  Main main;
  
  public BattleWindow(Main main, String type)
  {
    super("March of the Little Boxes: " + type);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setResizable(true);
    
    this.main = main;
    placeComponents(type);
    pack();
    setLocationRelativeTo(null);
    
    battle.start();
  }
  
  private void placeComponents(String type)
  {
    JPanel content = new JPanel(new BorderLayout());
    // content.setOpaque(true);

    battle = new Battle(this);   
    ControlPanel controlPanel;
    
    if ("Campaign".equals(type))
    {
      controlPanel = new CampaignPanel(battle, main, this);
    }
    else
    {
      controlPanel = new ControlPanel(battle, main, this);
      battle.setColor(Battle.SAND);
    }
    
    battle.setControlPanel(controlPanel);
    
    JPanel battlePanel = new JPanel();
    battlePanel.setBorder(BorderFactory.createTitledBorder("Battle"));
    battlePanel.add(battle);
    
    content.add(controlPanel, BorderLayout.CENTER);
    content.add(battlePanel, BorderLayout.EAST);

    setContentPane(content);
  }

  public Battle getBattle()
  {
    return battle;
  }
  
}
