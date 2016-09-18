import java.util.ArrayList;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;

public class SheduleView extends JPanel
{ private JButton back;
    private Fr fr;
    private  Color []colors = {Color.gray,Color.green,Color.red};
    public SheduleView(Fr fr)
    {
        this.fr=fr;
        setLayout(new BorderLayout());
        //    ImageIcon backTime=new ImageIcon("back.png");
        //   backTime=new ImageIcon(fr.getScaledImage(backTime.getImage(),(int)(fr.height/15), (int)(fr.height/15)));
        //      back = new JButton(backTime);
        //             back = new JButton(backTime);
        createGUI();
    }

    ArrayList   <MyJPanel> myPanels;
    private void createGUI(){
        fr.getContentPane().removeAll();
        fr.getContentPane().add(this);

        JPanel header=new JPanel();
        // JPanel bottom=new JPanel();
        // bottom.setLayout(new BorderLayout());

        header.setLayout(new BorderLayout());
        header.add(fr.home,BorderLayout.LINE_END);
        //  header.add(back,BorderLayout.LINE_START);

        JPanel center=new JPanel();
        myPanels=new <MyJPanel> ArrayList();
        add(header,BorderLayout.PAGE_START);
        // add(bottom,BorderLayout.PAGE_END);
        ArrayList<String>[] usingList;
        String neededOutputs;
        usingList=fr.sh.outputPowerCommands;
        neededOutputs=fr.sh.getAllCommandOutput();
        String [] outputs=neededOutputs.split("@@@");
        center.setLayout(new GridLayout((int)Math.sqrt(usingList.length)+1,(int)Math.sqrt(usingList.length)+1));
        for(int i=0;i<usingList.length;i++){
            ArrayList <String> list=usingList[i];
            MyJPanel button = new MyJPanel(list.get(0));
            myPanels.add(button);
            center.add(button);
            add(center);
        }

        repaint();
        revalidate();
        fr.repaint();
        fr.revalidate();

    }
    private class MyJPanel extends JPanel{
        int color_id=0;
        String title ;
        private JPanel centerPanel= new JPanel();
        private JScrollPane scrollSpecific;
        public MyJPanel(String title){
            this.title=title;
                        scrollSpecific = new JScrollPane(centerPanel);
            setLayout(new BorderLayout());
            setBorder(BorderFactory.createLineBorder(Color.black));
            JLabel titleLabel= new JLabel(title);
            JPanel titlePanel= new JPanel();
            titlePanel.add(titleLabel);
            setBackground(colors[color_id]);
            titlePanel.setBackground(colors[color_id]);
            add(titlePanel,BorderLayout.PAGE_START);
          //  add(centerPanel);
                        add(scrollSpecific,BorderLayout.CENTER);
            centerPanel.setLayout(new BoxLayout(centerPanel,BoxLayout.Y_AXIS));
            // example of use
            for(int j=0;j<10;j++){
            SingleSheduleView ssv=new SingleSheduleView();
            centerPanel.add(ssv);
        }
            //## end of example

        }

        private class SingleSheduleView extends JPanel{

            private String activeDays, time, weeklyString, activeString,commandString;
            int id;
            protected JPanel firstRow,secondRow;
private JButton delete,edit;
private Checkbox isWeekly,isActive; 
            public SingleSheduleView(){
                
                setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
                            setBorder(BorderFactory.createLineBorder(Color.blue));
                firstRow= new JPanel();
                secondRow= new JPanel();
                delete= new JButton("delete");
                edit= new JButton("edit");
                isWeekly= new Checkbox("is Weekly");
                isActive= new Checkbox("is isActive");
                add(firstRow);
                add(secondRow);
                for(int i=0;i<7;i++){
                    JButton dayButton=null;
                    switch(i){
                        case 0:
                        dayButton= new JButton("Su");
                        break;
                        case 1:
                        dayButton= new JButton("Mo");
                        break;
                        case 2:
                        dayButton= new JButton("Tu");
                        break;
                        case 3:
                        dayButton= new JButton("We");
                        break;
                        case 4:
                        dayButton= new JButton("Th");
                        break;
                        case 5:
                        dayButton= new JButton("Fr");
                        break;
                        case 6:
                        dayButton= new JButton("Sa");
                        break;    
                        default: 
                        dayButton= new JButton("Uknown");

                    }
                    firstRow.add(dayButton);
                    dayButton.setBackground(colors[0]);
                }
                
                secondRow.add(edit);
                                secondRow.add(isWeekly);
                                                secondRow.add(isActive);
                secondRow.add(delete);

            }
        }

    } 
}
