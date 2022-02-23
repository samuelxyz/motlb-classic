package mechanics;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import entity.Entity;
import runner.Main;

/**
 * File: src/mechanics/ControlPanel.java
 * <P>
 * Controls a {@code battle} and displays as a pane on the left side of the window.
 * This implementation is for sandbox mode.
 * 
 * @author Samuel Tan
 */
public class ControlPanel extends JPanel
  implements ActionListener
{
  protected Battle battle;
  private JButton startStop, clearAll, resurrectAll, graphicsOptions, lineTool, backToMenu;
  private JLabel previewLabel;
  protected JScrollPane actionSelector, teamSelector;
  protected JList<String> actionList, teamList;
  private JLabel teamLabel;
  protected JPanel bottom;
  protected JLabel messageHint;
  protected String[] types;
  protected Main main;
  protected BattleWindow window;
  protected GraphicsOptions graphicsDialog;
  
  public ControlPanel(Battle battle, Main main, BattleWindow window)
  {
    super();
//    setSize(200, 800);
    setOpaque(true);
    setPreferredSize(new Dimension(250, 800));
//    setMinimumSize(new Dimension(200, 800));
//    setMaximumSize(new Dimension(200, 800));
    
    this.battle = battle;
    this.main = main;
    this.window = window;
    
    setTypes(0);
    placeComponents();
  }
  
  protected void placeComponents()
  {
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setBorder(BorderFactory.createTitledBorder("Controls"));
    startStop = new JButton("Start/Stop Battle");
    startStop.setActionCommand("start/stop");
    startStop.addActionListener(this);
    
    clearAll = new JButton("Clear all");
    clearAll.setActionCommand("clear all");
    clearAll.addActionListener(this);
    
    resurrectAll = new JButton("Resurrect all");
    resurrectAll.setActionCommand("resurrect all");
    resurrectAll.addActionListener(this);
    
    graphicsOptions = new JButton("Graphics options...");
    graphicsOptions.setActionCommand("graphics options");
    graphicsOptions.addActionListener(this);
    
    previewLabel = new JLabel("Select unit type or action:");
    
    initializeActionSelector();
    
    teamLabel = new JLabel("Select team:");
    
    String[] teams = new String[Entity.TEAMS];
    for (int i = 0; i < teams.length; i++)
    {
      teams[i] = Entity.teamName(i) + " Team";
    }
    
    teamList = new JList<String>(teams);
    teamList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    teamList.setLayoutOrientation(JList.VERTICAL);
    teamList.setVisibleRowCount(-1);
    teamList.addListSelectionListener(new ListSelectionListener()
    {
      public void valueChanged(ListSelectionEvent e)
      {
        battle.setSelectedTeam(teamList.getSelectedIndex());
        battle.refreshLineTool();
      }
    });
    teamList.setSelectedIndex(0);
    teamSelector = new JScrollPane(teamList);
//    teamSelector.setPreferredSize(new Dimension(180, 4 + 18*Entity.TEAMS));
    teamSelector.setPreferredSize(new Dimension(180, 
      (int) teamList.getCellBounds(0, teams.length-1).getHeight()+2));
    
    initializeActionSelector();
    
    lineTool = new JButton("Line tool");
    lineTool.setActionCommand("line tool");
    lineTool.addActionListener(this);
    
    bottom = new JPanel(new BorderLayout());
    
    messageHint = new JLabel();
    
    backToMenu = new JButton("Quit to Main Menu");
    backToMenu.setActionCommand("menu");
    backToMenu.addActionListener(this);

    addLeft(startStop, this);
    addSpace(5, this);
    addLeft(clearAll, this);
    addSpace(5, this);
    addLeft(resurrectAll, this);
    addSpace(5, this);
    addLeft(graphicsOptions, this);
    addSpace(12, this);
    addLeft(previewLabel, this);
    addSpace(5, this);
    addLeft(actionSelector, this);
    addSpace(12, this);
    addLeft(teamLabel, this);
    addSpace(5, this);
    addLeft(teamSelector, this);
    addSpace(12, this);
    addLeft(lineTool, this);
    addSpace(5, this);
    bottom.add(messageHint, BorderLayout.NORTH);
//    bottom.add(Box.createRigidArea(new Dimension(0, 12)), BorderLayout.SOUTH);
    bottom.add(backToMenu, BorderLayout.SOUTH);
    addLeft(bottom, this);
  }
  
  /**
   * Constructs {@code actionList} and {@code actionSelector}.
   */
  protected void initializeActionSelector()
  {
    actionList = new JList<String>(types);
    actionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    actionList.setLayoutOrientation(JList.VERTICAL);
    actionList.setVisibleRowCount(-1);
    actionList.addListSelectionListener(new ListSelectionListener()
    {
      public void valueChanged(ListSelectionEvent e)
      {
        battle.setAction((String)actionList.getSelectedValue());
        battle.refreshLineTool();
      }
    });
    
    actionSelector = new JScrollPane(actionList);
//    actionSelector.setPreferredSize(new Dimension(180, 4 + 18*types.length));
    actionSelector.setPreferredSize(new Dimension(180, 
        (int) actionList.getCellBounds(0, types.length-1).getHeight()+2));
  }

  protected void addLeft(JComponent c, JComponent target)
  {
    c.setAlignmentX(Component.LEFT_ALIGNMENT);
    target.add(c);
  }
  
  protected void addSpace(int height, JComponent target)
  {
    target.add(Box.createRigidArea(new Dimension(0, height)));
  }

  public void actionPerformed(ActionEvent e)
  {
    if (e.getActionCommand().equals("start/stop")) 
    {
      battle.setPaused(!battle.isPaused()); // toggle
    }
    else if (e.getActionCommand().equals("clear all"))
    {
      battle.clearAll();
    }
    else if (e.getActionCommand().equals("resurrect all"))
    {
      battle.resurrectAll();
    }
    else if (e.getActionCommand().equals("graphics options"))
    {
      if (graphicsDialog == null)
      {
        graphicsDialog = new GraphicsOptions(window, battle, this);
        graphicsDialog.setVisible(true);
      }
    }
    else if (e.getActionCommand().equals("line tool"))
    {
      setText("Click line endpoints");
      battle.startLineTool();
    }
    else if (e.getActionCommand().equals("menu"))
    {
      backToMain();
    }
  }
  
  public void setText(String s)
  {
    messageHint.setText(s);
  }
  public String text()
  {
    return messageHint.getText();
  }
  
  /**
   * Determines the different units available on each level.
   * Level 0 indicates sandbox mode; other levels will be
   * implemented by overriding this method.
   * <P>
   * IMPORTANT: For a unit to be available in sandbox mode,
   * its name must be listed here.
   */
  protected void setTypes(int level)
  {
    switch (level)
    {
    case 0:
      types = new String[] {
          "Remove (click target)", 
          "Melee Unit", 
          "Juggernaut", 
          "Ranged Unit", 
          "Smart Ranged Unit",
          "Laser Unit",
          "Cannon",
          "Shield Bearer",
          "Charger",
          "Resurrector"
      };
      return;
    }
  }

  public String[] types()
  {
    return types;
  }
  
  public void clearGraphicsDialog()
  {
    graphicsDialog = null;
  }

  public void backToMain()
  {
    main.setVisible(true);
    battle.stop();
//    window.setVisible(false);
    window.dispose();
  }
  
}
