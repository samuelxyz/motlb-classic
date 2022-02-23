package mechanics;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import mechanics.lineTool.LineTool;
import runner.Main;

/**
 * File: src/mechanics/CampaignPanel.java
 * <P>
 * A version of the Sandbox {@code ControlPanel}, 
 * with different controls to be used for campaign mode.
 * Includes level selection and level loading.
 * 
 * @author Samuel Tan
 *
 */
public class CampaignPanel extends ControlPanel
{
  protected int level, resources, levelResources;
  protected JLabel resourceLabel;
  protected JList<String> levelList;
  protected JTabbedPane pane;
  
  public CampaignPanel(Battle battle, Main main, BattleWindow window)
  {
    super(battle, main, window);
    level = 0;
    setTypes(level);
  }

  /** 
   * Specifies the enemy arrangements, team zone, and
   * background color on each campaign level.
   * <P>
   * This method will set {@code battle.selectedTeam} to 1 (blue/player team).
   * Additionally, the battle will be paused.
   */
  protected void loadLevel(int level)
  {
    battle.setPaused(true);
    battle.clearAll();
    battle.setSelectedTeam(0);
    
    switch (level)
    {
    case 1: // the basics
      levelResources = 5;
      battle.setColor(Battle.GRASS);
      battle.setAction("Melee Unit");
      battle.newUnit(new Vector2D(400, 200), Math.PI/2, true);
      break;
    case 2: // divide and conquer
      levelResources = 15;
      battle.setColor(Battle.GRASS);
      battle.setAction("Melee Unit");
      LineTool.doLineTool(battle, new Vector2D(300, 200), new Vector2D(500, 200), LineTool.EAST, 4);
      battle.setTeamArea(new BoundingBox(new Vector2D(200, 100), new Vector2D(600, 300)));
      break;
    case 3: // hold the line?
      levelResources = 35;
      battle.setColor(Battle.GRASS);
      battle.setAction("Melee Unit");
      LineTool.doLineTool(battle, new Vector2D(200, 100), new Vector2D(600, 100), LineTool.EAST, 10);
      battle.setTeamArea(new BoundingBox(new Vector2D(), new Vector2D(799.9, 200)));
      break;
    case 4: // rapidfire
      levelResources = 60;
      battle.setColor(Battle.SAND);
      battle.setAction("Melee Unit");
      LineTool.doLineTool(battle, new Vector2D(250, 200), new Vector2D(550, 200), LineTool.EAST, 5);
      battle.setAction("Ranged Unit");
      LineTool.doLineTool(battle, new Vector2D(325, 100), new Vector2D(475, 100), LineTool.EAST, 3);
      battle.setTeamArea(new BoundingBox(new Vector2D(150, 0), new Vector2D(650, 300)));
      break;
    case 5: // monkey see
      levelResources = 60;
      battle.setColor(Battle.SAND);
      battle.setAction("Melee Unit");
      LineTool.doLineTool(battle, new Vector2D(250, 100), new Vector2D(550, 100), LineTool.EAST, 5);
      battle.setAction("Ranged Unit");
      LineTool.doLineTool(battle, new Vector2D(200, 200), new Vector2D(600, 200), LineTool.EAST, 2);
      battle.setTeamArea(new BoundingBox(new Vector2D(0, 0), new Vector2D(799.9, 300)));
      break;
    case 6: // monsters
      levelResources = 40;
      battle.setColor(Battle.SAND);
      battle.setAction("Melee Unit");
      LineTool.doLineTool(battle, new Vector2D(250, 450), new Vector2D(550, 450), LineTool.EAST, 5);
      battle.setAction("Juggernaut");
      battle.newUnit(new Vector2D(400, 350), 0, true);
      battle.setTeamArea(new BoundingBox(new Vector2D(150, 200), new Vector2D(650, 600)));
      break;
    case 7: // crossfire
      levelResources = 100;
      battle.setColor(Battle.SAND);
      battle.setAction("Ranged Unit");
      LineTool.doLineTool(battle, new Vector2D(250, 300), new Vector2D(380, 100), LineTool.EAST, 5);
      LineTool.doLineTool(battle, new Vector2D(550, 300), new Vector2D(420, 100), LineTool.EAST, 5);
      battle.setTeamArea(new BoundingBox(new Vector2D(150, 50), new Vector2D(650, 350)));
      break;
    case 8: // lateral
      levelResources = 50;
      battle.setColor(Battle.DIRT);
      battle.setAction("Smart Ranged Unit");
      LineTool.doLineTool(battle, new Vector2D(100, 100), new Vector2D(200, 100), LineTool.EAST, 3);
      battle.setAction("Melee Unit");
      LineTool.doLineTool(battle, new Vector2D (500, 100), new Vector2D(700, 100), LineTool.EAST, 5);
      battle.setTeamArea(new BoundingBox(new Vector2D(0, 0), new Vector2D(799.9, 200)));
      break;
    case 9: // from behind
      levelResources = 100;
      battle.setColor(Battle.DIRT);
      battle.setAction("Melee Unit");
      LineTool.doLineTool(battle, new Vector2D(200, 100), new Vector2D(600, 100), LineTool.EAST, 7);
      battle.setAction("Ranged Unit");
      LineTool.doLineTool(battle, new Vector2D(150, 200), new Vector2D(650, 200), LineTool.EAST, 2);
      battle.setAction("Smart Ranged Unit");
      battle.newUnit(new Vector2D(400, 700), 0, true);
      battle.setTeamArea(new BoundingBox(new Vector2D(0, 0), new Vector2D(799.9, 300)));
      break;
    case 10: // the horde
      levelResources = 100;
      battle.setColor(Battle.GRASS);
      battle.setAction("Melee Unit");
      LineTool.doLineTool(battle, new Vector2D(100, 50), new Vector2D(700, 50), LineTool.EAST, 11);
      LineTool.doLineTool(battle, new Vector2D(100, 100), new Vector2D(700, 100), LineTool.EAST, 11);
      LineTool.doLineTool(battle, new Vector2D(100, 150), new Vector2D(700, 150), LineTool.EAST, 11);
      battle.setTeamArea(new BoundingBox(new Vector2D(0, 0), new Vector2D(799.9, 200)));
      break;
    case 11: // stonewalling
      levelResources = 100;
      battle.setColor(Battle.GRASS);
      battle.setAction("Shield Bearer");
      LineTool.doLineTool(battle, new Vector2D(200, 100), new Vector2D(600, 100), LineTool.EAST, 7);
      battle.setAction("Smart Ranged Unit");
      LineTool.doLineTool(battle, new Vector2D(267, 50), new Vector2D(533, 50), LineTool.EAST, 3);
      battle.setAction("Ranged Unit");
      LineTool.doLineTool(battle, new Vector2D(100, 100), new Vector2D(700, 100), LineTool.EAST, 2);
      battle.setTeamArea(new BoundingBox(new Vector2D(0, 0), new Vector2D(799.9, 250)));
      break;
    case 12: // fight in the shade
      levelResources = 100;
      battle.setColor(Battle.STONE);
      battle.setAction("Ranged Unit");
      LineTool.doLineTool(battle, new Vector2D(100, 100), new Vector2D(700, 100), LineTool.EAST, 13);
      battle.setTeamArea(new BoundingBox(new Vector2D(0, 0), new Vector2D(799.9, 200)));
      break;
    case 13: // unbalanced
      levelResources = 100;
      battle.setColor(Battle.SAND);
      battle.setAction("Cannon");
      LineTool.doLineTool(battle, new Vector2D(675, 100), new Vector2D(700, 100), LineTool.EAST, 2);
      battle.setAction("Melee Unit");
      LineTool.doLineTool(battle, new Vector2D(200, 100), new Vector2D(600, 100), LineTool.EAST, 9);
      battle.setAction("Juggernaut");
      battle.newUnit(new Vector2D(100, 100), Math.PI/2, false);
      battle.setTeamArea(new BoundingBox(new Vector2D(0, 0), new Vector2D(799.9, 200)));
      break;
    case 14: // bait
      levelResources = 70;
      battle.setColor(Battle.SAND);
      battle.setAction("Cannon");
      battle.newUnit(new Vector2D(400, 200), 0, true);
      battle.setAction("Melee Unit");
      LineTool.doLineTool(battle, new Vector2D(200, 300), new Vector2D(300, 300), LineTool.EAST, 3);
      LineTool.doLineTool(battle, new Vector2D(500, 300), new Vector2D(600, 300), LineTool.EAST, 3);
      battle.setTeamArea(new BoundingBox(new Vector2D(100, 100), new Vector2D(700, 400)));
      break;
    case 15: // fish in a barrel?
      levelResources = 70;
      battle.setColor(Battle.STONE);
      battle.setAction("Melee Unit");
      LineTool.doLineTool(battle, new Vector2D(350, 350), new Vector2D(450, 350), LineTool.EAST, 5);
      LineTool.doLineTool(battle, new Vector2D(350, 375), new Vector2D(450, 375), LineTool.EAST, 5);
      LineTool.doLineTool(battle, new Vector2D(350, 400), new Vector2D(450, 400), LineTool.EAST, 5);
      LineTool.doLineTool(battle, new Vector2D(350, 425), new Vector2D(450, 425), LineTool.EAST, 5);
      LineTool.doLineTool(battle, new Vector2D(350, 450), new Vector2D(450, 450), LineTool.EAST, 5);
      battle.setTeamArea(new BoundingBox(new Vector2D(300, 300), new Vector2D(500, 500)));
      break;
    case 16: // hammer and anvil
      levelResources = 100;
      battle.setColor(Battle.SNOW);
      battle.setAction("Melee Unit");
      LineTool.doLineTool(battle, new Vector2D(200, 100), new Vector2D(600, 100), LineTool.EAST, 9);
      battle.setAction("Juggernaut");
      battle.newUnit(new Vector2D(100, 100), Math.PI/2, false);
      battle.newUnit(new Vector2D(700, 100), Math.PI/2, false);
      battle.setAction("Charger");
      LineTool.doLineTool(battle, new Vector2D(200, 790), new Vector2D(600, 790), LineTool.WEST, 5);
      battle.setTeamArea(new BoundingBox(new Vector2D(), new Vector2D(799.9, 200)));
      break;
    case 17: // dropping like flies
      levelResources = 100;
      battle.setColor(Battle.SNOW);
      battle.setAction("Melee Unit");
      LineTool.doLineTool(battle, new Vector2D(200, 100), new Vector2D(600, 100), LineTool.EAST, 9);
      battle.setAction("Ranged Unit");
      LineTool.doLineTool(battle, new Vector2D(100, 100), new Vector2D(700, 100), LineTool.EAST, 2);
      battle.setAction("Laser Unit");
      battle.newUnit(new Vector2D(400, 50), 0, true);
      battle.setTeamArea(new BoundingBox(new Vector2D(), new Vector2D(799.9, 400)));
      break;
    case 18: // touch of death
      levelResources = 100;
      battle.setColor(Battle.GRASS);
      battle.setAction("Melee Unit");
      LineTool.doLineTool(battle, new Vector2D(200, 200), new Vector2D(600, 200), LineTool.EAST, 9);
      LineTool.doLineTool(battle, new Vector2D(250, 250), new Vector2D(550, 250), LineTool.EAST, 7);
      battle.setAction("Ranged Unit");
      LineTool.doLineTool(battle, new Vector2D(200, 250), new Vector2D(600, 250), LineTool.EAST, 2);
      battle.setAction("Resurrector");
      battle.newUnit(new Vector2D(400, 100), 0, true);
      battle.setTeamArea(new BoundingBox(new Vector2D(50, 0), new Vector2D(750, 400)));
      break;
    case 19: // postmortal
      levelResources = 150;
      battle.setColor(Battle.GRASS);
      battle.setAction("Juggernaut");
      LineTool.doLineTool(battle, new Vector2D(200, 100), new Vector2D(600, 100), LineTool.EAST, 5);
      battle.setAction("Ranged Unit");
      LineTool.doLineTool(battle, new Vector2D(100, 150), new Vector2D(200, 150), LineTool.EAST, 3);
      LineTool.doLineTool(battle, new Vector2D(600, 150), new Vector2D(700, 150), LineTool.EAST, 3);
      battle.setAction("Smart Ranged Unit");
      LineTool.doLineTool(battle, new Vector2D(300, 50), new Vector2D(500, 50), LineTool.EAST, 3);
      battle.setTeamArea(new BoundingBox(new Vector2D(), new Vector2D(799.9, 200)));
      break;
    case 20: // columnar
      levelResources = 100;
      battle.setColor(Battle.SAND);
      battle.setAction("Charger");
      LineTool.doLineTool(battle, new Vector2D(400, 25), new Vector2D(400, 300), LineTool.NORTH, 12);
      LineTool.doLineTool(battle, new Vector2D(300, 150), new Vector2D(300, 350), LineTool.EAST, 5);
      LineTool.doLineTool(battle, new Vector2D(500, 150), new Vector2D(500, 350), LineTool.WEST, 5);
      battle.setTeamArea(new BoundingBox(new Vector2D(100, 0), new Vector2D(700, 400)));
      break;
    case 21: // toeholds
      levelResources = 100;
      battle.setColor(Battle.SAND);
      battle.setAction("Ranged Unit");
      LineTool.doLineTool(battle, new Vector2D(100, 350), new Vector2D(700, 350), LineTool.WEST, 7);
      LineTool.doLineTool(battle, new Vector2D(100, 450), new Vector2D(700, 450), LineTool.EAST, 7);
      battle.setTeamArea(new BoundingBox(new Vector2D(50, 50), new Vector2D(750, 750)));
      break;
    case 22: // propulsion
      levelResources = 100;
      battle.setColor(Battle.STONE);
      battle.setAction("Shield Bearer");
      LineTool.doLineTool(battle, new Vector2D(300, 150), new Vector2D(500, 150), LineTool.EAST, 7);
      battle.setAction("Charger");
      LineTool.doLineTool(battle, new Vector2D(300, 125), new Vector2D(500, 125), LineTool.EAST, 7);
      battle.setTeamArea(new BoundingBox(new Vector2D(), new Vector2D(799.9, 200)));
      break;
    case 23: // crushers
      levelResources = 100;
      battle.setColor(Battle.DIRT);
      battle.setAction("Juggernaut");
      LineTool.doLineTool(battle, new Vector2D(100, 150), new Vector2D(700, 150), LineTool.EAST, 7);
      battle.setTeamArea(new BoundingBox(new Vector2D(), new Vector2D(799.9, 200)));
      break;
    case 24: // autofocus
      levelResources = 100;
      battle.setColor(Battle.SAND);
      battle.setAction("Smart Ranged Unit");
      LineTool.doLineTool(battle, new Vector2D(100, 150), new Vector2D(700, 150), LineTool.EAST, 13);
      battle.setTeamArea(new BoundingBox(new Vector2D(), new Vector2D(799.9, 300)));
      break;
    case 25: // who's who
      levelResources = 100;
      battle.setColor(Battle.GRASS);
      setTypes(0);
      for (int i = 0; i < types.length; i++)
      {
        battle.setAction(types[i]);
        battle.newUnit(new Vector2D(100 + i * 600.0/types.length, 100), Math.PI/2, false);
      }
      battle.setTeamArea(new BoundingBox(new Vector2D(), new Vector2D(799.9, 200)));
    }
    
    resources = levelResources;
    refreshResourceLabel();
    battle.setAction((String)actionList.getSelectedValue());
    battle.setSelectedTeam(1);
  }
  
  /**
   * Specifies different units available in each campaign level.
   * The price of a unit is specified by the number in brackets.
   * <P>
   * Entries must be in the format {@code "[price] Unit Name"} for the
   * game to determine the prices properly.
   * {@code "Unit Name"} also works, and is equivalent to 
   * {@code "[0] Unit Name"}.
   */
  @Override
  protected void setTypes(int level)
  {
    super.setTypes(level); // shouldn't be necessary in campaign mode
    
    switch (level)
    {
    case 1: // the basics
    case 2: // divide and conquer
    case 3: // hold the line?
    case 4: // rapidfire
      types = new String[] {
          "Remove (click target)", 
          "[5] Melee Unit"
      };
      return;
    case 5: // monkey see
    case 6: // monsters
      types = new String[] {
          "Remove (click target)",
          "[5] Melee Unit",
          "[20] Ranged Unit"
      };
      return;
    case 7: // crossfire
      types = new String[] {
          "Remove (click target)",
          "[5] Melee Unit",
          "[20] Ranged Unit",
          "[25] Juggernaut"
      };
      return;
    case 8: // lateral
      types = new String[] {
          "Remove (click target)",
          "[8] Melee Unit",
          "[12] Ranged Unit",
          "[25] Juggernaut"
      };
    case 9: // from behind
      types = new String[] {
          "Remove (click target)",
          "[10] Melee Unit",
          "[20] Ranged Unit",
          "[25] Juggernaut",
          "[30] Smart Ranged Unit"
      };
      return;
    case 10: // the horde
    case 11: // stonewalling
      types = new String[] {
          "Remove (click target)",
          "[5] Melee Unit",
          "[25] Ranged Unit",
          "[25] Juggernaut",
          "[15] Smart Ranged Unit"
      };
      return;
    case 12: // fight in the shade
      types = new String[] {
          "Remove (click target)",
          "[5] Melee Unit",
          "[25] Ranged Unit",
          "[25] Juggernaut",
          "[15] Smart Ranged Unit",
          "[30] Shield Bearer"
      };
      return;
    case 13: // unbalanced
      types = new String[] {
          "Remove (click target)",
          "[5] Melee Unit",
          "[20] Ranged Unit",
          "[25] Juggernaut",
          "[15] Smart Ranged Unit",
          "[30] Shield Bearer"
      };
      return;
    case 14: // bait
    case 15: // fish in a barrel?
    case 16: // hammer and anvil
      types = new String[] {
          "Remove (click target)",
          "[5] Melee Unit",
          "[20] Ranged Unit",
          "[25] Juggernaut",
          "[15] Smart Ranged Unit",
          "[30] Shield Bearer",
          "[30] Cannon"
      };
      return;
    case 17: // dropping like flies
      types = new String[] {
          "Remove (click target)",
          "[5] Melee Unit",
          "[20] Ranged Unit",
          "[25] Juggernaut",
          "[15] Smart Ranged Unit",
          "[30] Shield Bearer",
          "[30] Cannon",
          "[25] Charger"
      };
      return;
    case 18: // touch of death
      types = new String[] {
        "Remove (click target)",
        "[5] Melee Unit",
        "[20] Ranged Unit",
        "[25] Juggernaut",
        "[15] Smart Ranged Unit",
        "[30] Shield Bearer",
        "[30] Cannon",
        "[25] Charger",
        "[35] Laser Unit"
      };
    return;
    case 19: // postmortal
    case 20: // columnar
    case 21: // toeholds
    case 22: // propulsion
    case 23: // crushers
    case 24: // autofocus
    case 25: // who's who
      types = new String[] {
        "Remove (click target)",
        "[5] Melee Unit",
        "[15] Ranged Unit",
        "[25] Juggernaut",
        "[15] Smart Ranged Unit",
        "[30] Shield Bearer",
        "[30] Cannon",
        "[25] Charger",
        "[35] Laser Unit",
        "[40] Resurrector"
      };
    return;
    }
  }

  /**
   * @param delta
   * @return true if enough resources remain to do the increment
   */
  public boolean incrementResources(int delta)
  {
    if (resources + delta < 0)
      return false;
    
    resources += delta;
    refreshResourceLabel();
    return true;
  }
  
  public int remainingResources()
  {
    return resources;
  }
  
  protected void refreshResourceLabel()
  {
    resourceLabel.setText("Remaining: " + resources + " / " + levelResources);
  }
  
  @Override
  protected void placeComponents()
  { 
    setLayout(new BorderLayout());
    
    pane = new JTabbedPane();
    
    makeLevelTab();
    makePlayTab(null);
    
    this.add(pane, BorderLayout.CENTER);
    
    JButton back = new JButton("Quit to Main Menu");
    back.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        backToMain();
      }
    });
    
    this.add(back, BorderLayout.SOUTH);
  }
  
  /**
   * The level names are set in here.
   */
  protected void makeLevelTab()
  {
    JPanel levelTab = new JPanel();
    levelTab.setLayout(new BoxLayout(levelTab, BoxLayout.Y_AXIS));
    levelTab.setBorder(BorderFactory.createTitledBorder("Campaign levels"));
    
    String[] levelNames = {
        "Level 1: The basics",
        "Level 2: Divide and conquer",
        "Level 3: Hold the line?",
        "Level 4: Rapidfire",
        "Level 5: Monkey see",
        "Level 6: Monsters",
        "Level 7: Crossfire",
        "Level 8: Lateral",
        "Level 9: From behind",
        "Level 10: The horde",
        "Level 11: Stonewalling",
        "Level 12: Fight in the shade",
        "Level 13: Unbalanced",
        "Level 14: Bait",
        "Level 15: Fish in a barrel?",
        "Level 16: Hammer and anvil",
        "Level 17: Dropping like flies",
        "Level 18: Touch of death",
        "Level 19: Postmortal",
        "Level 20: Columnar",
        "Level 21: Toeholds",
        "Level 22: Propulsion",
        "Level 23: Crushers",
        "Level 24: Autofocus",
        "Level 25: Who's who"
    };
    
    levelList = new JList<String>(levelNames);
    levelList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    levelList.setLayoutOrientation(JList.VERTICAL);
    levelList.setVisibleRowCount(-1);
    levelList.addListSelectionListener(new ListSelectionListener()
    {
      public void valueChanged(ListSelectionEvent e)
      {
//        if (e.getValueIsAdjusting() == false )
        {
          level = levelList.getSelectedIndex() + 1; // list indices start at 0
//          System.out.println("Level selected: " + level);
          
          loadLevel(level);
          setTypes(level);
          makePlayTab((String)levelList.getSelectedValue());
        }
      }
    });
    
    JScrollPane levelSelector = new JScrollPane(levelList);
    
    JButton play = new JButton("Play level");
    play.addActionListener(new ActionListener() 
    {
      public void actionPerformed(ActionEvent e)
      {
        pane.setSelectedIndex(1);
      }
    });
    
    JPanel bottom = new JPanel(new BorderLayout());
    
    bottom.add(levelSelector, BorderLayout.CENTER);
    bottom.add(play, BorderLayout.SOUTH);
    
    addLeft(bottom, levelTab);
    
    pane.addTab("Level Select", levelTab);
  }
  
  protected void makePlayTab(String name)
  {
    JPanel playTab = new JPanel();
    playTab.setLayout(new BoxLayout(playTab, BoxLayout.Y_AXIS));
    
    if (name == null)
    {
      addLeft(new JLabel("Select a level to play it"), playTab);
      pane.addTab("Play Level", playTab);
      
      // initialize
      resourceLabel = new JLabel();
      messageHint = new JLabel();
      initializeActionSelector();
      return;
    }
    
    playTab.setBorder(BorderFactory.createTitledBorder(name));
    
    JButton startStop = new JButton("Start/Stop Battle");
    startStop.setActionCommand("start/stop");
    startStop.addActionListener(this);
    
    JButton reset = new JButton("Reset Level");
    reset.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        loadLevel(level);
      }
    });
    
    JButton graphicsOptions = new JButton("Graphics options...");
    graphicsOptions.setActionCommand("graphics options");
    graphicsOptions.addActionListener(this);
    
    initializeActionSelector();
    
    resourceLabel = new JLabel();
    refreshResourceLabel();
    
    JButton lineTool = new JButton("Line Tool");
    lineTool.setActionCommand("line tool");
    lineTool.addActionListener(this);
    
    messageHint = new JLabel();
    JButton levels = new JButton("Back to level select");
    levels.addActionListener(new ActionListener() 
    {
      public void actionPerformed(ActionEvent e)
      {
        pane.setSelectedIndex(0);
      }
    });
    JPanel bottom = new JPanel(new BorderLayout());
    
    addLeft(startStop, playTab);
    addLeft(reset, playTab);
    addLeft(graphicsOptions, playTab);
    addSpace(12, playTab);
    addLeft(new JLabel("Select unit type or action:"), playTab);
    addSpace(5, playTab);
    addLeft(actionSelector, playTab);
    addSpace(12, playTab);
    addLeft(resourceLabel, playTab);
    addSpace(12, playTab);
    addLeft(lineTool, playTab);
    addSpace(12, playTab);
    bottom.add(messageHint, BorderLayout.NORTH);
    bottom.add(levels, BorderLayout.SOUTH);
    addLeft(bottom, playTab);
    
    int tab = pane.indexOfTab("Play Level");
    if (tab == -1)
      pane.addTab("Play Level", playTab);
    else
      pane.setComponentAt(tab, playTab);
  }
  
}
