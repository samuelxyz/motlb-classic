package mechanics;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * File: src/mechanics/GraphicsOptions.java
 * <P>
 * A small pop-up dialog to control options 
 * for particles and antialiasing.
 * 
 * @author Samuel Tan
 *
 */
public class GraphicsOptions extends JDialog
{
  private Battle battle;
  private JButton particles, antialiasing;
  
  public GraphicsOptions(JFrame window, Battle battle, ControlPanel controlPanel)
  {
    super(window, "Graphics Options");
    
    this.battle = battle;
    
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() 
    {
      public void windowClosing(WindowEvent e) 
      {
        controlPanel.clearGraphicsDialog();
        dispose();
      }
    });
    setResizable(false);
    
    JPanel content = new JPanel(new BorderLayout());
    JPanel buttons = new JPanel();
    buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));
    JPanel bottom = new JPanel();
    
    particles = new JButton();
    updateParticleButton();
    particles.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        battle.setParticlesEnabled(!battle.particlesEnabled());
        updateParticleButton();
      }
    });
    
    antialiasing = new JButton();
    updateAntialiasingButton();
    antialiasing.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        battle.setAntialiasing(!battle.antialiasing());
        updateAntialiasingButton();
      }
    });
    
    buttons.add(particles);
    buttons.add(antialiasing);
    buttons.setBorder(BorderFactory.createTitledBorder("Options"));
    
    content.add(buttons, BorderLayout.CENTER);
    
    JButton ok = new JButton("OK");
    ok.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        controlPanel.clearGraphicsDialog();
        dispose();
      }
    });
    
    bottom.add(ok);
    content.add(bottom, BorderLayout.SOUTH);
    
    content.setPreferredSize(new Dimension(200, 130));
    
    setContentPane(content);
    pack();
    setLocationRelativeTo(window);
  }
  
  private void updateParticleButton()
  {
    String newString = (battle.particlesEnabled())? "Enabled" : "Disabled";
    particles.setText("Particles: " + newString);
  }
  
  private void updateAntialiasingButton()
  {
    String newString = (battle.antialiasing())? "Enabled" : "Disabled";
    antialiasing.setText("Antialiasing: " + newString);
  }
}
