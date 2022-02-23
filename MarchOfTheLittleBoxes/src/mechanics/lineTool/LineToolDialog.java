package mechanics.lineTool;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * File: src/mechanics/lineTool/LineToolDialog.java
 * <P>
 * Allows the user to control a {@code LineTool}.
 * 
 * @author Samuel Tan
 *
 */
public class LineToolDialog extends JDialog implements ActionListener
{
  private LineTool lineTool;
  private JButton plus;
  private JButton minus;
  private JButton flip;
  private JButton cancel;
  private JButton confirm;
  private JTextField field;
  private int fieldNum = 1;

  public LineToolDialog(Frame window, LineTool lineTool)
  {
    super(window, "Line Tool: Options");

    this.lineTool = lineTool;
    
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() 
    {
      public void windowClosing(WindowEvent e) 
      {
        lineTool.dispose();
      }
    });

    JPanel content = new JPanel(new BorderLayout());
    JPanel adjuster = new JPanel();
    JPanel actions = new JPanel();

    field = new JTextField("" + fieldNum, 2);
    lineTool.makeLine(fieldNum);
    field.setActionCommand("update");
    field.addActionListener(lineTool);

    plus = makeButton("+");
    minus = makeButton("-");
    flip = makeButton("Flip direction");
    cancel = makeButton("Cancel");
    confirm = makeButton("OK");

    adjuster.add(flip);
    adjuster.add(field);
    adjuster.add(plus);
    adjuster.add(minus);
    content.add(adjuster, BorderLayout.CENTER);

    actions.add(cancel);
    actions.add(confirm);

    content.add(actions, BorderLayout.PAGE_END);

    this.setContentPane(content);
    pack();
    setResizable(false);
    setLocationRelativeTo(window);
    setVisible(true);
  }

  /**
   * helper method
   */
  private JButton makeButton(String name)
  {
    JButton b = new JButton(name);
    b.setActionCommand(name);
    b.addActionListener(lineTool);
    b.addActionListener(this);
    return b;
  }

  public void actionPerformed(ActionEvent e)
  {
    if (e.getSource() == plus)
    {
      int i = Integer.parseInt(field.getText());
      i++;
      field.setText("" + i);
      fieldNum = i;
      lineTool.makeLine(i);
    }
    else if (e.getSource() == minus)
    {
      int i = Integer.parseInt(field.getText());
      if (i <= 1)
        return;
      i--;
      field.setText("" + i);
      fieldNum = i;
      lineTool.makeLine(i);
    }
    else if (e.getSource() == field)
    {
      int i;
      try
      {
        i = Integer.parseInt(field.getText());
      }
      catch (NumberFormatException ex)
      {
        field.setText("" + fieldNum);
        return;
      }

      if (i <= 0)
        return;

      fieldNum = i;
      lineTool.makeLine(i);
    }
  }

}